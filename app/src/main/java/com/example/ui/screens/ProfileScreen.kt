package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsGolf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyChartEntry
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.theme.GolfGoldAccent

@Composable
fun ProfileScreen(
    currentUser: UserProfile?,
    playActivity: List<DailyChartEntry> = emptyList(),
    onUpdateProfile: (name: String, phone: String, address: String, extra: String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(currentUser) { mutableStateOf(currentUser?.name ?: "") }
    var phone by remember(currentUser) { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var address by remember(currentUser) { mutableStateOf(currentUser?.address ?: "") }
    var extra by remember(currentUser) { mutableStateOf(currentUser?.experienceOrHandicap ?: "") }
    var savedSuccess by remember { mutableStateOf(false) }

    val isCaddie = currentUser?.role == "caddie"

    GolfBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GolfGoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Member Profile",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Official club identity & round play activity",
                        fontSize = 12.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }

                OutlinedButton(
                    onClick = onLogout,
                    border = BorderStroke(1.dp, Color(0xFFFF8A80)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("logout_btn")
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFFF8A80), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Switch ID", color = Color(0xFFFF8A80), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User ID Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xEE0B1E12)),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GolfGoldAccent),
                modifier = Modifier.fillMaxWidth().testTag("profile_badge_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(if (isCaddie) Color(0x33D4AF37) else Color(0x332E7D32))
                            .border(2.dp, if (isCaddie) GolfGoldAccent else Color(0xFF81C784), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCaddie) Icons.Default.Badge else Icons.Default.SportsGolf,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = currentUser?.name ?: "Member",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Club ID: ",
                                color = Color(0xAAFFFFFF),
                                fontSize = 12.sp
                            )
                            Text(
                                text = currentUser?.id ?: "N/A",
                                color = GolfGoldAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                color = if (isCaddie) Color(0x44D4AF37) else Color(0x442E7D32),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = (currentUser?.role ?: "golfer").uppercase(),
                                    color = if (isCaddie) GolfGoldAccent else Color(0xFFC8E6C9),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Play Activity Section ("Play with Golfer Name Fully Displayed")
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xEE0B1E12)),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFF81C784)),
                modifier = Modifier.fillMaxWidth().testTag("profile_play_activity_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCaddie) "Play with Golfer Activity" else "Round & Caddie Activity",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            color = Color(0x3381C784),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${playActivity.size} Rounds Recorded",
                                color = Color(0xFFA5D6A7),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = if (isCaddie)
                            "Complete history of golfers you played with across all daily charts"
                        else
                            "Complete history of caddies assigned for your golf rounds",
                        color = Color(0xFFA5D6A7),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (playActivity.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.GolfCourse, contentDescription = null, tint = Color(0x6681C784), modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isCaddie) "No golfer rounds assigned yet" else "No caddie assignments yet",
                                    color = Color(0xAAFFFFFF),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Pairings are generated automatically on daily charts",
                                    color = Color(0x66FFFFFF),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            playActivity.forEach { entry ->
                                Surface(
                                    color = Color(0x5508170D),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Color(0x3381C784)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(13.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = entry.date,
                                                    color = GolfGoldAccent,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = entry.timeSlot.ifBlank { "Tee Time" },
                                                    color = Color(0xFFA5D6A7),
                                                    fontSize = 11.sp
                                                )
                                            }

                                            if (entry.autoFinishTime.isNotBlank()) {
                                                Surface(
                                                    color = Color(0x332E7D32),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "Finished ${entry.autoFinishTime}",
                                                        color = Color(0xFF81C784),
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            } else {
                                                Surface(
                                                    color = Color(0x33D4AF37),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "Active / In Play",
                                                        color = GolfGoldAccent,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Prominent Display of Golfer Name (as requested: "Play with golfer name Fully display in their profile activity")
                                        if (isCaddie) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Played with Golfer: ",
                                                    color = Color(0xAAFFFFFF),
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = entry.golferName,
                                                    color = Color.White,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                            if (entry.phoneNumber.isNotBlank() || entry.address.isNotBlank()) {
                                                Text(
                                                    text = "Golfer Contact: ${entry.phoneNumber} • ${entry.address}",
                                                    color = Color(0x88FFFFFF),
                                                    fontSize = 10.sp
                                                )
                                            }
                                        } else {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Assigned Caddie: ",
                                                    color = Color(0xAAFFFFFF),
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = entry.caddieName,
                                                    color = Color.White,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                            val cadNum = if (entry.caddieNumber.isNotBlank()) entry.caddieNumber else entry.caddieId
                                            if (cadNum.isNotBlank() || entry.caddieAadhaar.isNotBlank()) {
                                                Text(
                                                    text = "Caddie No: ${cadNum.ifBlank { "N/A" }} • Aadhaar: ${entry.caddieAadhaar.ifBlank { "N/A" }}",
                                                    color = GolfGoldAccent,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                        if (entry.notes.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Round Notes: ${entry.notes}",
                                                color = Color(0xFFC8E6C9),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Edit Form
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xDD0D2415)),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0x3381C784)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Profile Information",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Update your official details for daily charts & pairing",
                        color = Color(0xFFA5D6A7),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; savedSuccess = false },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(18.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x4481C784)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; savedSuccess = false },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(18.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x4481C784)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_phone_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Address
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it; savedSuccess = false },
                        label = { Text("Address / City") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(18.dp)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x4481C784)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_address_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Handicap / Experience
                    OutlinedTextField(
                        value = extra,
                        onValueChange = { extra = it; savedSuccess = false },
                        label = { Text(if (isCaddie) "Caddie Experience / Rating" else "Golfer Handicap / Skill Level") },
                        placeholder = { Text(if (isCaddie) "e.g. 5 Years PGA Experience" else "e.g. Handicap 8 (Scratch)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x4481C784)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_extra_input")
                    )

                    if (savedSuccess) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Profile saved successfully!", color = Color(0xFF81C784), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onUpdateProfile(name.trim(), phone.trim(), address.trim(), extra.trim())
                                savedSuccess = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("save_profile_btn")
                    ) {
                        Text("Save Profile Changes", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
