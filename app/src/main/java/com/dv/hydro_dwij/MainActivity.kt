package com.dv.hydro_dwij

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.dv.hydro_dwij.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.binding.btnCalculate.setOnClickListener(this)
        this.binding.btnReset.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnCalculate -> this.calculateElectricityBill()
            R.id.btnReset -> this.resetResult()
        }
    }

    fun calculateElectricityBill() {
        val morningConsumption = this.binding.etMrngUsage.text.toString()
        val eveningConsumption = this.binding.etEvngUsage.text.toString()

        if(morningConsumption.isNullOrEmpty()) {
            this.binding.etMrngUsage.error = "This field is required"
            this.binding.tvError.text = "Error: All fields must be filled in"
        } else if(eveningConsumption.isNullOrEmpty()) {
            this.binding.etEvngUsage.error = "This field is required"
            this.binding.tvError.text = "Error: All fields must be filled in"
        } else {
            val morningConsumptionInt = morningConsumption.toFloat()
            val eveningConsumptionInt = eveningConsumption.toFloat()

            val morningCost = morningConsumptionInt * 0.132
            val eveningCost = eveningConsumptionInt * 0.094
            val totalUsageCost = morningCost + eveningCost

            val isEnvRebate = this.binding.swEnvRebate.isChecked
            var envRebateAmt = 0.0

            var subtotal: Double
            if(isEnvRebate == true) {
                envRebateAmt = (totalUsageCost * 9)/100
                subtotal = totalUsageCost - envRebateAmt
            } else {
                subtotal = totalUsageCost
            }

            val taxAmt = (subtotal * 13)/100
            val totalAmt = subtotal + taxAmt

            this.binding.tvOutputHeader.text = "Charge Breakdown"

            val outputString = "Morning Usage Cost: $${String.format("%.2f", morningCost)}\n" +
                    "Evening Usage Cost: $${String.format("%.2f", eveningCost)}\n" +
                    "Total Usage Charge: $${String.format("%.2f", totalUsageCost)}\n" +
                    "Environmental Rebate: $${String.format("%.2f", envRebateAmt)}\n" +
                    "Subtotal: $${String.format("%.2f", subtotal)}\n" +
                    "Sales Tax: $${String.format("%.2f", taxAmt)}\n"

            this.binding.tvFinalAmtOp.text = "You must Pay: $${String.format("%.2f", totalAmt)}"
            this.binding.tvOutput.text = outputString
        }
    }

    fun resetResult() {
        this.binding.etMrngUsage.text.clear()
        this.binding.etEvngUsage.text.clear()
        this.binding.swEnvRebate.isChecked = false
        this.binding.tvOutput.text = ""
        this.binding.tvOutputHeader.text = ""
        this.binding.tvError.text = ""
        this.binding.tvFinalAmtOp.text = ""
    }
}