package a16924.a16931

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_dish_detail.*

class DishDetail : AppCompatActivity()
{
    var chefDetail = ""
    var id = ""
    var nameDetail = ""
    var stepsDetail  = ""
    var cookingTimeDetail = ""
    var ingredientsDetail = ""
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("users").child("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dish_detail)

        intent.extras?.let{bundle ->
            chefDetail = bundle.getString("chef_detail")!!
            nameDetail = bundle.getString("name_detail")!!
            stepsDetail = bundle.getString("steps_detail")!!
            cookingTimeDetail = bundle.getString("cooking_time_detail")!!
            ingredientsDetail = bundle.getString("ingredients_detail")!!
            id = bundle.getString("id")!!
        }
    if(id == "1")
    {
    remove.isVisible = false
    }
        textChefDetail.text = chefDetail
        textNameDetail.text = nameDetail
        textStepsDetail.text = stepsDetail
        textTimeDetail.text = cookingTimeDetail
        textIngredientsDetail.text = ingredientsDetail


        remove.setOnClickListener {
            myRef.child(id).removeValue()
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_share -> {

                val shareIntent: Intent
                shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, nameDetail)
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Chef:"+chefDetail+"-Steps:"+stepsDetail+"-CookTime:"+cookingTimeDetail+"Ingredients:"+ingredientsDetail)
                startActivity(Intent.createChooser(shareIntent, "Share"))

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
