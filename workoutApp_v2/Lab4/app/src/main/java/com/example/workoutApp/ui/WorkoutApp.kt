package com.example.workoutApp.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutApp(){
    //dummy weather data
    //val weatherData = arrayOf("Today - Storm 8 / 12", "Tomorrow - Foggy 9 / 13", "Thurs - Rainy 8 / 13", "Fri - foggy 8 / 12", "Sat - Sunny 9 / 14", "Sun - Sunny 10 / 15", "Mon - Sunny 11 / 15")
    val viewModel: WorkoutViewModel = viewModel()
    val settingViewModel: SettingViewModel = viewModel()
    //create a NavController
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current
    val cityname by settingViewModel.cityname.observeAsState("")//observe the cityname from the preference datastore

    Scaffold (topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text("Workouts:")//Add a title to the app
            },
            actions = {
                IconButton(onClick = {
                //TODO
                    //get user input location
                    val location = "london"
                    //construct the uri
                    val geoUri = Uri.parse("geo:0,0?q=$cityname")//use cityname from preference datastore to view location on map
                    Log.d("uri",location)
                    //create an Intent
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        //set the uri data this intent is operating on
                        data = geoUri
                    }
                    //if there is an app that can handle the implicit intent
                    if (intent.resolveActivity(context.packageManager)!= null) {
                        context.startActivity(intent)
                    }
                }) {
                    Icon(Icons.Filled.Place, contentDescription = "View location on Map")
                }
                IconButton(onClick = {
                    //TODO
                    //Display a Toast
                    Toast.makeText(context, "Refreshing weather!", Toast.LENGTH_SHORT).show()
                    //val city = text
                    //Fetch weather data
                    viewModel.fetchForecastForCity(cityname)//use cityname from preference datastore to fetch weather from API
                    navController.navigate(route = AppScreens.Search.name)//navigate to WeatherSearchscreen to show the result
                    //Log the raw JSON response
                    //Log.d("ui State", weather)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Refresh weather")
                }
                IconButton(onClick = {
                    /*TODO*/
                    navController.navigate(route=AppScreens.Setting.name)
                }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Configure the default city")
                }
            }
        )
    }){innerPadding ->
        //add a NavHost
        NavHost(
            navController = navController,
            startDestination = AppScreens.Search.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreens.Search.name) {
                WorkoutSearchScreen(viewModel, navController)
            }

            // Route for WorkoutDetailScreen
            composable(
                route = "WorkoutDetailScreen/{index}",
                arguments = listOf(navArgument("index") { type = NavType.IntType })
            ) { backStackEntry ->
                val index = backStackEntry.arguments?.getInt("index")
                WorkoutDetailScreen(viewModel, index, navController)
            }

            // Route for ExerciseSpecScreen
            composable(
                route = "ExerciseSpecScreen/{workoutIndex}/{exerciseIndex}",
                arguments = listOf(
                    navArgument("workoutIndex") { type = NavType.IntType },
                    navArgument("exerciseIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val workoutIndex = backStackEntry.arguments?.getInt("workoutIndex") ?: 0
                val exerciseIndex = backStackEntry.arguments?.getInt("exerciseIndex") ?: 0
                ExerciseSpecScreen(viewModel, workoutIndex, exerciseIndex, navController)
            }

            composable(route = AppScreens.Setting.name) {
                WorkoutSettingScreen(settingViewModel)
            }
        }

    }
}

//create an enum class to define the routes
enum class AppScreens{
    Search, //Search represents WeatherSearchScreen
    Detail, //Detail represents WeatherDetailScreen
    Setting //Setting represents WeatherSettingScreen
}
