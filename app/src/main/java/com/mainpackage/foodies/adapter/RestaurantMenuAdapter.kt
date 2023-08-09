package com.mainpackage.foodies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mainpackage.foodies.R
import com.mainpackage.foodies.activity.CartActivity
import com.mainpackage.foodies.model.RestaurantMenu

class RestaurantMenuAdapter(
    val context: Context,
    private val restaurantId: String,
    private val restaurantName: String,
    private val proceedToCartPassed: RelativeLayout,
    private val buttonProceedToCart: Button,
    private val restaurantMenu: ArrayList<RestaurantMenu>
) : RecyclerView.Adapter<RestaurantMenuAdapter.ViewHolderRestaurantMenu>() {


    private var itemSelectedCount: Int = 0
    private lateinit var proceedToCart: RelativeLayout
    private var itemsSelectedId = arrayListOf<String>()

    class ViewHolderRestaurantMenu(view: View) : RecyclerView.ViewHolder(view) {
        val txtSerialNumber: TextView = view.findViewById(R.id.txtSerialNumber)
        val txtItemName: TextView = view.findViewById(R.id.txtItemName)
        val txtItemPrice: TextView = view.findViewById(R.id.txtItemPrice)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRestaurantMenu {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_menu_recycler_view_single_row, parent, false)

        return ViewHolderRestaurantMenu(view)
    }

    override fun getItemCount(): Int {
        return restaurantMenu.size
    }

    @SuppressLint("IntentWithNullActionLaunch", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderRestaurantMenu, position: Int) {

        val restaurantMenuItem = restaurantMenu[position]
        proceedToCart = proceedToCartPassed

        buttonProceedToCart.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("restaurantId", restaurantId)
            intent.putExtra("restaurantName", restaurantName)
            intent.putExtra("selectedItemsId", itemsSelectedId)
            context.startActivity(intent)
        }

        holder.btnAddToCart.setOnClickListener {

            if (holder.btnAddToCart.text.toString() == "Remove") {

                itemSelectedCount--
                itemsSelectedId.remove(holder.btnAddToCart.tag.toString())
                holder.btnAddToCart.text = "Add"
                holder.btnAddToCart.setBackgroundColor(Color.rgb(244, 67, 54))

            } else {

                itemSelectedCount++
                itemsSelectedId.add(holder.btnAddToCart.tag.toString())
                holder.btnAddToCart.text = "Remove"
                holder.btnAddToCart.setBackgroundColor( Color.rgb(255, 196, 0))
            }

            if (itemSelectedCount > 0) {
                proceedToCart.visibility = View.VISIBLE
            } else {
                proceedToCart.visibility = View.INVISIBLE
            }
        }

        holder.btnAddToCart.tag = restaurantMenuItem.id + ""
        holder.txtSerialNumber.text = (position + 1).toString()
        holder.txtItemName.text = restaurantMenuItem.name
        holder.txtItemPrice.text = "Rs. ${restaurantMenuItem.costForOne}"
    }

    fun getSelectedItemCount(): Int {
        return itemSelectedCount
    }

}