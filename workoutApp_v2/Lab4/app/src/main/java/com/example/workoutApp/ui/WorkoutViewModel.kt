package com.example.workoutApp.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutApp.Exercise
import com.example.workoutApp.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import javax.net.ssl.HttpsURLConnection
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutViewModel : ViewModel() {
//    workout information - will be gotten from memory
    //TODO - this will eventually relate to storage

    private val _workoutData = MutableStateFlow<List<Workout>>(emptyList())
    val workoutData = _workoutData.asStateFlow()  // Expose as read-only StateFlow
    init {
        _workoutData.value = getDummyWorkouts() // Or set from repository
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDummyWorkouts(): List<Workout> {
        return listOf(
            Workout(
                title = "Morning Routine",
                date = LocalDate.of(2025, 5, 4),
                exercises = listOf(
                    Exercise(name = "Push-ups", weight = 12.5, reps = 20, notes = "Good form", videoUri = null),
                    Exercise(name = "Squats", weight = 180.0, reps = 4, notes = "Focus on depth", videoUri = null)
                )
            ),
            Workout(
                title = "Evening Routine",
                date = LocalDate.of(2025, 5, 4),
                exercises = listOf(
                    Exercise(name = "Sit-ups", weight = 0.0, reps = 30, notes = "Engage core", videoUri = null),
                    Exercise(name = "Plank", weight = 0.0, reps = 60, notes = "Hold as long as you can", videoUri = null)
                )
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadWorkouts() {
        // Assuming your dummy workouts are fetched
        _workoutData.value = getDummyWorkouts()  // Update the workout data
    }



//below is for getting the weather information

    //private mutable _weatherData with an initial empty list of Strings, backing property of weatherData
    //weatherData will be fetched from the Internet using Kotlin coroutines, use State Flow as it works seamlessly with coroutines
    private val _uiState = MutableStateFlow("")
    //Publicly exposed as a read-only StateFlow
    val uiState = _uiState.asStateFlow()
    //now the ui state is an array of Strings
    private val _weatherData = MutableStateFlow<Array<String>>(emptyArray())
    val weatherData = _weatherData.asStateFlow()


    private val apiKey = "Your_Own_API_Key"
    // Function to fetch and update the forecast data
    fun fetchForecastForCity(city: String) {
        viewModelScope.launch (Dispatchers.IO) {
           try {
                val (lat,lon) = getCoordinates(city)
                Log.d("launch coordinates","$lat, $lon")
                val url2 = "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&units=metric&appid=$apiKey"
                Log.d("launch url2", url2)
                val result = getJSONFromApi(url2) // Your suspend function to fetch data
                _uiState.value = result//result JSON string

                _weatherData.value = getWeatherDataFromJson (result) //Parse the JSON string into the Array of Strings

            } catch (e: Exception) {
                // Handle exceptions
                Log.d("launch","exception")
                e.printStackTrace()
            }

        }
    }

    // Send a GET request to the URL, fetch weather forecast, returns the response in JSON string
    private fun getJSONFromApi(url: String): String {
        var result = ""
        var conn: HttpsURLConnection? = null
        try {
            val request = URL(url)
            conn = request.openConnection() as HttpsURLConnection
            Log.d("suspend function","create a connection")
            conn.connect()
            Log.d("suspend function","connect")
            val inStrea: InputStream = conn.inputStream
            result = convertInputStreamToString(inStrea)
            Log.d("suspend function",result)
        } catch (e: Exception) {
            Log.d ("suspend function", "exception")
            result = "Network Error! Please check network connection."
            e.printStackTrace()
        }
        finally {

            conn?.disconnect()
        }
        return result //returns the fetched JSON string
    }

    // The helper function that converts the input stream to String
    @Throws(IOException::class)
    private fun convertInputStreamToString(inS: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inS))
        val result = StringBuilder()
        var line: String?

        // Read out the input stream buffer line by line until it's empty
        while (bufferedReader.readLine().also { line = it } != null) {
            result.append(line)
        }
        Log.d("convert",result.toString())

        // Close the input stream and return
        inS.close()
        return result.toString()
    }
    //The helper function that converts city name to co-ordinates
    private fun getCoordinates(city:String):Pair<String,String>{
        //build the URL using the user entered city
        val url1 = "https://api.openweathermap.org/geo/1.0/direct?q=$city&appid=$apiKey"
        Log.d("getCoordinates url1", url1)
        var lat = ""
        var lon = ""
        try {
            val response = getJSONFromApi(url1)
            Log.d("getCoordinates",response)
            val cityArray = JSONArray(response)
            val coordinates = cityArray.getJSONObject(0)
            lat = coordinates.getDouble("lat").toString()
            Log.d("getCoordinates",lat)
            lon = coordinates.getDouble("lon").toString()
            Log.d("getCoordinates",lon)
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(lat,lon)
    }

    //Take the raw json string and pull out the data we need, and construct a list of Strings with them
    private fun getWeatherDataFromJson(jsonStr: String): Array<String> {
        val resultStrs = Array(40){""}//5 days/3 hr forecast returns an array of size 40
        try {
            // These are the names of the JSON objects that need to be extracted
            val owmList = "list"
            val owmDateTime = "dt"
            val owmMain = "main"
            val owmTemp = "temp"
            val owmWeather = "weather"
            val owmDescription = "description"

            val forecastJson = JSONObject(jsonStr)
            val weatherArray: JSONArray = forecastJson.getJSONArray(owmList)

            for (i in 0 until weatherArray.length()) {
                // Format "Day, description, temp"
                val eachForecast = weatherArray.getJSONObject(i)
                //Log.d("parse JSON", dayForecast.toString())

                val dateTime = eachForecast.getLong(owmDateTime)
                val dayHour = getReadableDateString(dateTime)
                Log.d("parse JSON", dayHour)

                val mainObject = eachForecast.getJSONObject(owmMain)
                val temp = mainObject.getDouble(owmTemp).roundToInt()
                Log.d("parse JSON",temp.toString())

                val weatherObject = eachForecast.getJSONArray(owmWeather).getJSONObject(0)
                val description = weatherObject.getString(owmDescription)
                Log.d("parse JSON",description)
                resultStrs[i] = "$dayHour - $description - $temp°C"
                Log.d("parse JSON",resultStrs[i])
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyArray<String>()
        }
        return resultStrs
    }

    private fun getReadableDateString(time: Long): String {
        val date = Date(time * 1000)
        val format = SimpleDateFormat("E, MMM d, ha")
        return format.format(date)
    }





    fun saveExercise(workoutIndex: Int, exerciseIndex: Int?, newExercise: Exercise) {
        //this is where the saving will happen
        //TODO - change this to effect storage, not just dummy data
        val currentWorkouts = _workoutData.value.toMutableList()

        if (workoutIndex !in currentWorkouts.indices) return  // Prevent crashes on invalid index

        val workout = currentWorkouts[workoutIndex]
        val updatedExercises = workout.exercises.toMutableList()

        //either replace or add
        if (exerciseIndex != null && exerciseIndex in updatedExercises.indices) {
            //replace existing exercise
            updatedExercises[exerciseIndex] = newExercise
        } else {
            //add new exercise
            updatedExercises.add(newExercise)
        }

        val updatedWorkout = workout.copy(exercises = updatedExercises)

        //replace the workout in the list
        currentWorkouts[workoutIndex] = updatedWorkout

        //new list
        _workoutData.value = currentWorkouts

    }

fun saveWorkout(){//TODO - populate

}
}