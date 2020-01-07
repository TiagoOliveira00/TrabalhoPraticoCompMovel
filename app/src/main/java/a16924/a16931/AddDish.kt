package a16924.a16931

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_dish.*
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class AddDish: AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    var bit : Bitmap?=null
    var dish : Dish? = null
    var generator = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dish)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_Add -> {
                dish = Dish("",edit_text_add_owner.text.toString(),edit_text_add_name.text.toString(),edit_text_time.text.toString())
                dish?.AdicionarPassos(edit_text_add_steps.text.toString())
                dish?.AdicionarIngredientes(edit_text_Ing.text.toString())

                dish?.id = MainActivity.count.toString()
                val intent = Intent()
                intent.putExtra(DISH,dish!!.toJson().toString())
                setResult(MainActivity.ACTIVITY_RESULT_CODE,intent)

                var s : String = MainActivity.count.toString()
                myRef.child("users").child(s).setValue(dish)
                finish()
                return true
            }
            R.id.menu_photo->{

                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                        //permission was not enabled
                        val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //show popup to request permission
                        ActivityCompat.requestPermissions(this,permission, PERMISSION_CODE)

                    }
                    else{
                        //permission already granted
                        openCamera()
                    }
                }
                else{
                    //system os is < marshmallow
                    openCamera()
                }
               dish?.image = image_uri
                return true}
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, 0)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        bit = data!!.extras!!.get("data") as Bitmap
        imageView2.setImageBitmap(data!!.extras!!.get("data")as Bitmap)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
           val extra : Bundle ?= data!!.getExtras()

            when(requestCode){
                PERMISSION_CODE -> {
                    if (resultCode == Activity.RESULT_OK && data != null ){
                        //permission from popup was granted
                        imageView2.setImageBitmap(data.extras!!.get("data")as Bitmap)
                        openCamera()
                    }
                    else{
                        //permission from popup was denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    companion object {
        val DISH = "a16924.a16931.dish"
    }

}