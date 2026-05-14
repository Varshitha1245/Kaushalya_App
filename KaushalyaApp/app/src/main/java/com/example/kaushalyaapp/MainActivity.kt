package com.example.kaushalyaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var screen by remember { mutableStateOf("start") }

            when (screen) {

                "start" -> StartScreen(
                    onLogin = { screen = "login" },
                    onRegister = { screen = "register" }
                )

                "register" -> RegisterScreen {
                    screen = "login"
                }

                "login" -> LoginScreen {
                    screen = "main"
                }

                "main" -> MainScreen()
            }
        }
    }
}

//////////////////////////////////////////////////////
// START SCREEN
//////////////////////////////////////////////////////

@Composable
fun StartScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Kaushalya", fontSize = 34.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Text("Skill Marketplace")

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

//////////////////////////////////////////////////////
// REGISTER
//////////////////////////////////////////////////////

@Composable
fun RegisterScreen(onDone: () -> Unit) {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Create Account", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(phone)
                    .set(
                        mapOf(
                            "name" to name,
                            "phone" to phone,
                            "password" to password
                        )
                    )

                Toast.makeText(
                    context,
                    "Account Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                onDone()

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}

//////////////////////////////////////////////////////
// LOGIN
//////////////////////////////////////////////////////

@Composable
fun LoginScreen(onLogin: () -> Unit) {

    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Login", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Logged In Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                onLogin()

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

//////////////////////////////////////////////////////
// MAIN SCREEN
//////////////////////////////////////////////////////

@Composable
fun MainScreen() {

    var tab by remember { mutableStateOf(0) }

    Scaffold(

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Text("🏠") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Text("➕") },
                    label = { Text("Add") }
                )

                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    icon = { Text("👤") },
                    label = { Text("Profile") }
                )
            }
        }

    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (tab) {

                0 -> HomeScreen()
                1 -> AddWorkerScreen()
                2 -> ProfileScreen()
            }
        }
    }
}

//////////////////////////////////////////////////////
// HOME SCREEN
//////////////////////////////////////////////////////

//////////////////////////////////////////////////////
// HOME SCREEN WITH FILTERS + SCROLL
//////////////////////////////////////////////////////

@Composable
fun HomeScreen() {

    val db = FirebaseFirestore.getInstance()

    var workers by remember {
        mutableStateOf(listOf<Worker>())
    }

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    val categories = listOf(
        "All",
        "Electrician",
        "Plumber",
        "Carpenter"
    )

    //////////////////////////////////////////////////////
    // FIREBASE FETCH
    //////////////////////////////////////////////////////

    LaunchedEffect(Unit) {

        db.collection("workers")
            .addSnapshotListener { value, _ ->

                if (value != null) {

                    workers = value.documents.mapNotNull {
                        it.toObject(Worker::class.java)
                    }
                }
            }
    }

    //////////////////////////////////////////////////////
    // FILTER WORKERS
    //////////////////////////////////////////////////////

    val filteredWorkers = if (selectedCategory == "All") {

        workers

    } else {

        workers.filter {
            it.category == selectedCategory
        }
    }

    //////////////////////////////////////////////////////
    // UI
    //////////////////////////////////////////////////////

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //////////////////////////////////////////////////////
        // TITLE
        //////////////////////////////////////////////////////

        Text(
            text = "Kaushalya",
            fontSize = 28.sp
        )

        Text(
            text = "Find Skilled Workers",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        //////////////////////////////////////////////////////
        // CATEGORY FILTERS
        //////////////////////////////////////////////////////

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            categories.forEach { category ->

                FilterChip(
                    selected = selectedCategory == category,

                    onClick = {
                        selectedCategory = category
                    },

                    label = {
                        Text(category)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        //////////////////////////////////////////////////////
        // WORKER LIST
        //////////////////////////////////////////////////////

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(filteredWorkers) { worker ->

                WorkerCard(worker)
            }
        }
    }
}
//////////////////////////////////////////////////////
// WORKER CARD
//////////////////////////////////////////////////////

@Composable
fun WorkerCard(worker: Worker) {

    var openChat by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(worker.name, fontSize = 22.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Text(worker.category)

            Spacer(modifier = Modifier.height(5.dp))

            Text(worker.description)

            Spacer(modifier = Modifier.height(15.dp))

            Button(onClick = {
                openChat = true
            }) {
                Text("Chat")
            }
        }
    }

    if (openChat) {
        PrivateChatScreen(worker.name)
    }
}

//////////////////////////////////////////////////////
// CHAT SCREEN
//////////////////////////////////////////////////////

@Composable
fun PrivateChatScreen(workerName: String) {

    val db = FirebaseFirestore.getInstance()

    var msg by remember { mutableStateOf("") }

    var messages by remember {
        mutableStateOf(listOf<String>())
    }

    LaunchedEffect(true) {

        db.collection("chat_$workerName")
            .addSnapshotListener { value, _ ->

                if (value != null) {

                    messages = value.documents.map {
                        it.getString("msg") ?: ""
                    }
                }
            }
    }

    AlertDialog(

        onDismissRequest = {},

        confirmButton = {},

        title = {
            Text("Chat with $workerName")
        },

        text = {

            Column {

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {

                    items(messages) {
                        Text(it)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = msg,
                    onValueChange = { msg = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {

                    db.collection("chat_$workerName")
                        .add(mapOf("msg" to msg))

                    msg = ""

                }) {
                    Text("Send")
                }
            }
        }
    )
}

//////////////////////////////////////////////////////
// ADD WORKER
//////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkerScreen() {

    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val categories = listOf(
        "Electrician",
        "Plumber",
        "Carpenter"
    )

    var selectedCategory by remember {
        mutableStateOf("Electrician")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Add Worker", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Worker Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                categories.forEach {

                    DropdownMenuItem(
                        text = {
                            Text(it)
                        },
                        onClick = {

                            selectedCategory = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                FirebaseFirestore.getInstance()
                    .collection("workers")
                    .add(
                        mapOf(
                            "name" to name,
                            "category" to selectedCategory,
                            "description" to description
                        )
                    )

                Toast.makeText(
                    context,
                    "Worker Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                name = ""
                description = ""

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

//////////////////////////////////////////////////////
// PROFILE SCREEN
//////////////////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val context = LocalContext.current

    var saved by remember {
        mutableStateOf(false)
    }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val professions = listOf(
        "Electrician",
        "Plumber",
        "Carpenter"
    )

    var selectedProfession by remember {
        mutableStateOf("Electrician")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    if (!saved) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Profile Settings", fontSize = 28.sp)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {

                OutlinedTextField(
                    value = selectedProfession,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Profession") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    professions.forEach {

                        DropdownMenuItem(
                            text = {
                                Text(it)
                            },
                            onClick = {

                                selectedProfession = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    FirebaseFirestore.getInstance()
                        .collection("profile")
                        .document(phone)
                        .set(
                            mapOf(
                                "name" to name,
                                "phone" to phone,
                                "address" to address,
                                "profession" to selectedProfession
                            )
                        )

                    Toast.makeText(
                        context,
                        "Profile Updated",
                        Toast.LENGTH_SHORT
                    ).show()

                    saved = true

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("My Profile", fontSize = 30.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Name : $name", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text("Phone : $phone", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text("Profession : $selectedProfession", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text("Address : $address", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                saved = false
            }) {
                Text("Edit Profile")
            }
        }
    }
}

//////////////////////////////////////////////////////
// DATA CLASS
//////////////////////////////////////////////////////

data class Worker(
    val name: String = "",
    val category: String = "",
    val description: String = ""
)