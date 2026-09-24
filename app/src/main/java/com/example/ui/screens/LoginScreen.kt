package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.theme.GolfGoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    golfers: List<UserProfile>,
    caddies: List<UserProfile>,
    onSelectUser: (UserProfile) -> Unit,
    onRegisterOrLogin: (id: String, name: String, role: String, phone: String, address: String, extra: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf("golfer") } // "golfer", "caddie", "admin"
    var manualIdInput by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    GolfBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // App Branding Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_golf_icon),
                    contentDescription = "Caddi's Day Logo",
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, GolfGoldAccent, RoundedCornerShape(14.dp))
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "caddi's day",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Daily Pairings • Time Charts • Caddie Records",
                        fontSize = 12.sp,
                        color = GolfGoldAccent,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Role Tab Selector (Golfer Login / Caddie Login / Admin)
            val tabs = listOf("Golfer Login", "Caddie Login", "Admin")
            val selectedTabIndex = when (selectedRole) {
                "golfer" -> 0
                "caddie" -> 1
                else -> 2
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0x880B1C10),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = GolfGoldAccent,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedRole = when (index) {
                                0 -> "golfer"
                                1 -> "caddie"
                                else -> "admin"
                            }
                            loginError = null
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) GolfGoldAccent else Color(0xCCFFFFFF)
                            )
                        },
                        modifier = Modifier.testTag("tab_login_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fast ID Login Input Box
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xE6132B1A)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4481C784)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = when (selectedRole) {
                            "golfer" -> "Select or Enter Golfer Login ID"
                            "caddie" -> "Select or Enter Caddie Login ID"
                            else -> "Enter Club Marshall / Admin ID"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = manualIdInput,
                            onValueChange = {
                                manualIdInput = it
                                loginError = null
                            },
                            placeholder = {
                                Text(
                                    text = when (selectedRole) {
                                        "golfer" -> "e.g. G-101"
                                        "caddie" -> "e.g. C-201"
                                        else -> "e.g. ADMIN-01"
                                    },
                                    color = Color(0x88FFFFFF),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Badge,
                                    contentDescription = null,
                                    tint = GolfGoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color(0x66FFFFFF),
                                cursorColor = GolfGoldAccent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("login_id_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (manualIdInput.isBlank()) {
                                    loginError = "Please enter an ID"
                                } else {
                                    val allPool = golfers + caddies
                                    val found = allPool.find { it.id.equals(manualIdInput.trim(), ignoreCase = true) }
                                    if (found != null) {
                                        onSelectUser(found)
                                    } else if (manualIdInput.trim().startsWith("ADMIN", ignoreCase = true)) {
                                        onRegisterOrLogin(
                                            manualIdInput.trim().uppercase(),
                                            "Marshall Officer",
                                            "admin",
                                            "+1 (555) 000-1111",
                                            "Clubhouse Security Suite",
                                            "Course Marshall"
                                        )
                                    } else {
                                        loginError = "ID not recognized. Select below or setup profile."
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("sign_in_id_btn")
                        ) {
                            Text(
                                text = "Sign In",
                                color = Color(0xFF1B241C),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (loginError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = loginError!!,
                            color = Color(0xFFFF8A80),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle for Member Directory Roster
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (selectedRole) {
                        "golfer" -> "Registered Golfers (${golfers.size})"
                        "caddie" -> "Registered Caddies (${caddies.size})"
                        else -> "Administrative Access"
                    },
                    color = GolfGoldAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                TextButton(
                    onClick = { showRegisterDialog = true },
                    modifier = Modifier.testTag("new_profile_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = Color(0xFFA5D6A7),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Setup Profile",
                        color = Color(0xFFA5D6A7),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // List of users for instant 1-tap login
            val displayUsers = when (selectedRole) {
                "golfer" -> golfers
                "caddie" -> caddies
                else -> emptyList()
            }

            if (selectedRole == "admin") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xCC200D0D)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44FF8A80)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Admin & Marshall Verification",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Club administrators have exclusive clearance to review confidential complaints and supervise daily auto-pairings.",
                            color = Color(0xCCFFFFFF),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                onRegisterOrLogin(
                                    "ADMIN-01",
                                    "Chief Marshall",
                                    "admin",
                                    "+1 (555) 000-9999",
                                    "Head Marshal Tower",
                                    "Course Superintendent"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("admin_instant_login")
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign In as Club Administrator", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayUsers, key = { it.id }) { user ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xCC0D2012)),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3381C784)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectUser(user) }
                                .testTag("user_item_${user.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (user.role == "golfer") Color(0x442E7D32) else Color(0x44D4AF37)
                                        )
                                        .border(
                                            1.dp,
                                            if (user.role == "golfer") Color(0xFF81C784) else GolfGoldAccent,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (user.role == "golfer") Icons.Default.SportsScore else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = user.name,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            color = Color(0x44000000),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = user.id,
                                                color = GolfGoldAccent,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    if (user.experienceOrHandicap.isNotBlank()) {
                                        Text(
                                            text = user.experienceOrHandicap,
                                            color = Color(0xFFC8E6C9),
                                            fontSize = 11.sp
                                        )
                                    }
                                    Text(
                                        text = "${user.phoneNumber} • ${user.address}",
                                        color = Color(0xAAFFFFFF),
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                                Button(
                                    onClick = { onSelectUser(user) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0x4481C784)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = "Log In",
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Guest Enter
            if (displayUsers.isNotEmpty()) {
                OutlinedButton(
                    onClick = { onSelectUser(displayUsers.first()) },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GolfGoldAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_enter_btn")
                ) {
                    Text(
                        text = "Quick Enter as ${displayUsers.first().name}",
                        color = GolfGoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    // New Profile Dialog
    if (showRegisterDialog) {
        ProfileSetupDialog(
            initialRole = selectedRole,
            onDismiss = { showRegisterDialog = false },
            onSave = { name, role, phone, address, exp, customId ->
                val idToUse = if (customId.isNotBlank()) customId
                else if (role == "golfer") "G-${100 + golfers.size + 1}"
                else "C-${200 + caddies.size + 1}"

                onRegisterOrLogin(idToUse, name, role, phone, address, exp)
                showRegisterDialog = false
            }
        )
    }
}

@Composable
fun ProfileSetupDialog(
    initialRole: String,
    onDismiss: () -> Unit,
    onSave: (name: String, role: String, phone: String, address: String, exp: String, customId: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(if (initialRole == "admin") "golfer" else initialRole) }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var extra by remember { mutableStateOf("") }
    var customId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF142B1B)),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Create Member Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Fill out name, phone number, address, and details",
                    fontSize = 12.sp,
                    color = GolfGoldAccent
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Role Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("golfer" to "Golfer", "caddie" to "Caddie").forEach { (rKey, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (role == rKey) GolfGoldAccent else Color(0x33000000))
                                .border(1.dp, if (role == rKey) GolfGoldAccent else Color(0x66FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { role = rKey }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (role == rKey) Color(0xFF1B241C) else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GolfGoldAccent,
                        unfocusedLabelColor = Color(0x88FFFFFF),
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GolfGoldAccent,
                        unfocusedLabelColor = Color(0x88FFFFFF),
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / City *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GolfGoldAccent,
                        unfocusedLabelColor = Color(0x88FFFFFF),
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = extra,
                    onValueChange = { extra = it },
                    label = { Text(if (role == "golfer") "Handicap (e.g. 12)" else "Caddie Experience (e.g. 3 years)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GolfGoldAccent,
                        unfocusedLabelColor = Color(0x88FFFFFF),
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customId,
                    onValueChange = { customId = it },
                    label = { Text("Custom Login ID (Optional)") },
                    placeholder = { Text(if (role == "golfer") "e.g. G-109" else "e.g. C-209", color = Color(0x55FFFFFF)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GolfGoldAccent,
                        unfocusedLabelColor = Color(0x88FFFFFF),
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = error!!, color = Color(0xFFFF8A80), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xAAFFFFFF))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isBlank() || phone.isBlank() || address.isBlank()) {
                                error = "Please complete Name, Phone, and Address"
                            } else {
                                onSave(name.trim(), role, phone.trim(), address.trim(), extra.trim(), customId.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Save & Log In", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

