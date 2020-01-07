package a16924.a16931

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.android.AndroidAuthTokenProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var dishes : MutableList<Dish> = ArrayList<Dish>()
    var dishesDisplay : MutableList<Dish> = ArrayList<Dish>()
    val auth = FirebaseAuth.getInstance()
    val listVDish = DishAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                dishes.clear()
                for (d in data.children){
                    if(d!=null && d.value != "End" ){
                    val dish = Dish("0","none","none","0")
                    dish.name = d.child("name").value.toString()
                    dish.chef = d.child("chef").value.toString()
                    dish.id = d.child("id").value.toString()
                    dish.ingredientes = d.child("ingredientes").value.toString()
                    dish.steps = d.child("steps").value.toString()
                    dish.time = d.child("time").value.toString()
                    dishes.add(dish)
                        dishesDisplay.add(dish)
                        listVDish.notifyDataSetChanged()
                    var i = d.child("id").value.toString().toInt()
                    count = i
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        listViewDish.adapter = listVDish

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_REQUEST_CODE){
            if (resultCode == ACTIVITY_RESULT_CODE){
                data?.getStringExtra(AddDish.DISH)?.let{

                    val dish = Dish(it)
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("users")

                    myRef.setValue(dish)

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.first_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)

            if (searchItem != null){
            val searchView = searchItem.actionView as SearchView
             //  val editext = searchView.findViewById<EditText>(R.id.menu_search)
               //   editext!!.hint = "Search here..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    dishesDisplay.clear()
                   if (newText!!.isNotEmpty()){
                        val search = newText.toLowerCase()
                        dishes.forEach{
                            if (it.name.toLowerCase().contains(search)){
                                dishesDisplay.add(it)
                            }
                        }
                       //dishesDisplay.addAll(dishes)
                        listVDish.notifyDataSetChanged()
                    } else {
                        dishesDisplay.addAll(dishes)
                        listVDish.notifyDataSetChanged()

                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu-> {
                val intent = Intent(this@MainActivity, AddDish::class.java)
                startActivity(intent)
                dishesDisplay.clear()
                MoreCount()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
    //Lista
    inner class DishAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parente: ViewGroup?): View {
            val v = layoutInflater.inflate(R.layout.activity_dish_list, parente, false)
            val textViewChef= v.findViewById<TextView>(R.id.textViewOwner)
            val textViewName = v.findViewById<TextView>(R.id.textViewDish_name)

            textViewChef.text = dishesDisplay[position].chef
            textViewName.text = dishesDisplay[position].name

            v.setOnClickListener {
                val intent = Intent(this@MainActivity, DishDetail::class.java)
                intent.putExtra("chef_detail", dishes[position].chef)
                intent.putExtra("name_detail", dishes[position].name)
                intent.putExtra("cooking_time_detail", dishes[position].time)
                intent.putExtra("steps_detail", dishes[position].steps)
                intent.putExtra("ingredients_detail", dishes[position].ingredientes)
                intent.putExtra("id",dishes[position].id)
                startActivity(intent)
                dishesDisplay.clear()
            }
            return  v
        }

        override fun getItem(position: Int): Any {
            return dishesDisplay[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return dishesDisplay.size
        }
}

fun GetCount() :Int
{
    return count
}
 fun MoreCount()
    {
        count++

    }
    companion object{
        val ACTIVITY_REQUEST_CODE = 1001
        val ACTIVITY_RESULT_CODE  = 1002
        val TAG = "MainActivity"
        var count = 0
    }
}
