package a16924.a16931

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import org.json.JSONObject

class Dish {
    var id  : String? = null
    var chef : String = "None"
    var name : String = "None"
    var time : String = "None"
    var ingredientes :String = "None"
    var steps : String = "None"
    var image : Uri? = null


    constructor(dataSnapshot: DataSnapshot){
        id  = dataSnapshot.key
        chef =  dataSnapshot.child("Chef").value.toString()
        name = dataSnapshot.child("Name").value.toString()
        time = dataSnapshot.child("Time").value.toString()
        ingredientes = dataSnapshot.child("Ingredients").value.toString()
        steps = dataSnapshot.child("Steps").value.toString()
    }
    constructor(id:String,ownerName : String , name : String , time : String)
    {
        this.id = id
        chef = ownerName
        this.name = name
        this.time = time
    }


    constructor(data:String){
        val jsonObject = JSONObject(data)
        id  = jsonObject.getString("id"         )
        chef = jsonObject.getString("Chef" )
        name = jsonObject.getString("Name" )
        time = jsonObject.getString("Time" )
        ingredientes = jsonObject.getString("Ingredients")
        steps = jsonObject.getString("Steps")
    }
    fun AdicionarIngredientes(ing : String)
    {
        ingredientes = ing
    }

    fun AdicionarPassos(steps : String )
    {
      this.steps = steps
    }
    fun toJson () : JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id" , id)
        jsonObject.put("Chef", chef)
        jsonObject.put("Name" , name)
        jsonObject.put("TIme" , time)
        jsonObject.put("Ingredients", ingredientes)
        jsonObject.put("Steps" , steps )
        return jsonObject
    }
}