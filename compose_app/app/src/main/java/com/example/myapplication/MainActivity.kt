package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.serpro69.kfaker.Faker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "userList"
    ) {
        composable("userList") {
            UserList(navController = navController)
        }
        composable("details/{userName}") { backStackEntry ->
            DetailsScreen(
                userName = backStackEntry.arguments?.getString("userName") ?: "",
                onBackClick = { navController.popBackStack() } // Обработчик нажатия кнопки "Назад"
            )
        }
    }
}

data class User(val name: String, val imageResId: Int)

val faker = Faker()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Demo Home Page") }, // Заголовок панели
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        val users = remember { List(100) { generateRandomUser() } }

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(users) { user ->
                UserListItem(user = user, onClick = {
                    navController.navigate("details/${user.name}")
                })
            }
        }
    }
}

fun generateRandomUser(): User {
    val adjective = faker.animal.name().lowercase().replaceFirstChar { it.uppercase() }
    val noun = faker.color.name().lowercase().replaceFirstChar { it.uppercase() }
    val name = "$adjective$noun"
    return User(
        name = name,
        imageResId = R.drawable.android_circle
    )
}

@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = user.imageResId),
            contentDescription = "User Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = user.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(userName: String, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_circle), // Замените на нужное изображение
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$userName details." +
                        "The screen is prepared for a Flutter workshop at the Droidcon Lisbon.",
                fontSize = 16.sp
            )
        }
    }
}