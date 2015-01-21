model.elecstore = function(balance)
{
    
    var store_capacity_remaining = store_max_capacity - model.elecstore_SOC;

    if (balance>0)
    {
        // STORE CHARGING
        //            Loss
        //             ^
        //             |
        // Surplus -l1---l2-> Store 
        //         
        // - l1 Rate limit
        // - l2 Capacity limit

        // Supply exceed's demand, excess energy fed into store
        var surplus_available = balance;
        
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var store_charge_rate_pre_loss = surplus_available;
        if (store_charge_rate_pre_loss>store_max_charge_rate) store_charge_rate_pre_loss = store_max_charge_rate;           // 1. Charge rate limit
        var store_charge_rate_after_loss = store_charge_rate_pre_loss * store_charge_efficiency;                            // 2. Calculate charge rate after losses
        if (store_charge_rate_after_loss>store_capacity_remaining) store_charge_rate_after_loss = store_capacity_remaining; // 3. Capacity limit
        store_charge_rate_pre_loss = store_charge_rate_after_loss / store_charge_efficiency;                                // 4. Work backwards to obtain pre loss charge rate again
        var store_loss = store_charge_rate_pre_loss - store_charge_rate_after_loss;                                         // 5. Calculate energy lost in charging
        model.elecstore_SOC += store_charge_rate_after_loss;                                                                // 6. Calculate new store capacity level
        
        return store_charge_rate_pre_loss;
    }
    else
    {
        // STORE DISCHARGE
        //            Loss
        //             ^
        //             |
        // Store -l2------l1-> Output 
        //         
        // - l1 Rate limit
        // - l2 Capacity limit
    
        var shortfall = -balance;

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        var store_discharge_rate_after_loss = shortfall;
        if (store_discharge_rate_after_loss>store_max_discharge_rate) store_discharge_rate_after_loss = store_max_discharge_rate;   // 1. Discharge rate limit
        var store_discharge_rate_pre_loss = store_discharge_rate_after_loss / store_discharge_efficiency;                           // 2. Calculate discharge rate pre losses
        if (store_discharge_rate_pre_loss>model.elecstore_SOC) store_discharge_rate_pre_loss = model.elecstore_SOC;                 // 3. Empty store limit
        store_discharge_rate_after_loss = store_discharge_rate_pre_loss * store_discharge_efficiency;                               // 4. Work backwards to obtain after loss charge rate again
        var store_loss = store_discharge_rate_pre_loss - store_discharge_rate_after_loss;                                           // 5. Calculate energy lost in charging
        model.elecstore_SOC -= store_discharge_rate_pre_loss;                                                                       // 6. Calculate new store capacity level
        
        return -store_discharge_rate_after_loss;
    }
}
