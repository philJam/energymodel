model.gasliquid = function(biomass, hydrogen, gas_demand, liquid_demand)
{
    //***************************************************************************************************
    // Algorithm to work out biofuels made, fossil fuels still required and residual emissions
    // Biomass to biogas efficiency 0.6, biomass to liquif biofuel efficiency 0.5 
    // Synth fuels assumption: SABATIER: 1 Biomass + 0.5 H2 --> 1 Biomethane. FISCHER-TROPSCH: 1 Biomass + 0.5 H2 --> 0.8 Synthetic liquid fuel.
        
    //-------------------------------------------------------------------------------------------
    // SUPPLY AS MUCH OF GAS AND LIQUID DEMAND FROM SABATIER & FISCHER-TROPSCH PROCESSES
    //-------------------------------------------------------------------------------------------

    // 1. start by calculating actual requirements of H2 and biomass if full demand was supplied
    // 2. then limit by the actual hydrogen available
    // 3. re-calcuate resultant biomass use
    // 4. then limit by biomass available
    // 5. re-calculate resultant H2 use
    // 6. calculate actual synthetic fuels produced

    // 1. Start by calculating actual requirements of H2 and biomass if full demand was supplied

    // var biomass_for_FT = (1.0/0.8) * liquid_demand;
    var H2_for_FT = (0.5/0.8) * liquid_demand;
    // var biomass_for_sabatier = (1.0) * gas_demand;
    var H2_for_sabatier = (0.5) * gas_demand;
    // var biomass_requirement = biomass_for_FT + biomass_for_sabatier;
    var H2_requirement = H2_for_FT + H2_for_sabatier;

    // 2. Limit by the actual amount of hydrogen available
    var H2 = hydrogen;                                                      // max H2 available 
    if (H2>H2_requirement) H2 = H2_requirement;                             // limit to demand if demand is larger than supply

    H2_for_FT = (H2_for_FT / H2_requirement) * H2;                          // calculate resulting proportion of available hydrogen available for FT
    H2_for_sabatier = (H2_for_sabatier/H2_requirement) * H2;                // and for sabatier process

    // 3. Calcuate resultant biomass use
    var biomass_for_FT = H2_for_FT / 0.5;
    var biomass_for_sabatier = H2_for_sabatier / 0.5;
    var biomass_requirement = biomass_for_FT + biomass_for_sabatier;

    // 4. then limit by biomass available if biomass requirement for hydrogen available is more than biomass available
    if (biomass_requirement > biomass)
    {
        biomass_for_FT = (biomass_for_FT/biomass_requirement) * biomass;
        biomass_for_sabatier = (biomass_for_sabatier/biomass_requirement) * biomass;
        
        // 5. re-calculate hydrogen requirement after biomass limit
        H2_for_FT = biomass_for_FT * 0.5;
        H2_for_sabatier = biomass_for_sabatier * 0.5;
    }

    biomass_requirement = biomass_for_FT + biomass_for_sabatier;
    H2_requirement = H2_for_FT + H2_for_sabatier;                    

    biomass -= biomass_requirement;
    hydrogen -= H2_requirement;

    // 6. calculate actual synthetic fuels produced
    var synthfuel = 0.8*biomass_for_FT;         // == 0.8*0.5*H2_for_FT;
    var biomethane = biomass_for_sabatier;

    // subtract available biomethane and synthfuel from demands
    gas_demand -= biomethane;
    liquid_demand -= synthfuel;

    //-------------------------------------------------------------------------------------------
    // SUPPLY REMAINING DEMAND BY LESS EFFICIENT DIRECT BIOMASS PROCESS
    //-------------------------------------------------------------------------------------------
    
    // Start by dividing remaining biomass by gas/liquid demand ratio:
    var max_biomass_for_gas = 0;
    if (gas_demand>0) max_biomass_for_gas = gas_demand/(gas_demand+liquid_demand) * biomass;            // biogas
    
    var max_biomass_for_liquid = 0;
    if (liquid_demand>0) max_biomass_for_liquid = liquid_demand/(gas_demand+liquid_demand) * biomass;   // biofuel

    var biogas = max_biomass_for_gas * 0.6;                         // calc max biogas production
    if (biogas>gas_demand) biogas = gas_demand;                       // Limit by gas demand
    biomass -= biogas / 0.6;                                        // calc actual biomass used

    var biofuel = max_biomass_for_liquid * 0.5;                     // calc max biofuel production
    if (biofuel>gas_demand) biofuel = liquid_demand;                  // Limit by liquid demand
    biomass -= biofuel / 0.5;                                       // calc actual biomass used

    gas_demand -= biogas;
    liquid_demand -= biofuel;
    
    return {
        "biomethane": biomethane,
        "biogas": biogas,
        "synthfuel": synthfuel,
        "biofuel": biofuel,
        
        "unmet_gas_demand": gas_demand,
        "unmet_liquid_demand": liquid_demand,
        
        "hydrogen_surplus": hydrogen,
        "biomass_surplus": biomass
    }
}
