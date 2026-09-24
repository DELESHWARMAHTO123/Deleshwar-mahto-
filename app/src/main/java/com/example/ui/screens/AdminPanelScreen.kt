package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.Complaint
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.components.GolfGlassCard
import com.example.ui.theme.GolfGoldAccent
import com.example.ui.theme.GolfGoldLight
import com.example.ui.theme.GolfGreenDark
import com.example.ui.theme.GolfGreenLight
import com.example.ui.theme.GolfGreenPrimary

@Composable
fun AdminPanelScreen(
    allUsers: List<UserProfile>,
    allComplaints: List<Complaint>,
    onAddUser: (id: String, name: String, role: String, phone: String, address: String, extra: String) -> Unit,
    onDeleteUser: (String) -> Unit,
    onNavigateToComplaints: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddMemberDialog by remember { mutableStateOf(false) }

    val golfersCount = allUsers.count { it.role == "golfer" }
    val caddiesCount = allUsers.count { it.role == "caddie" }
    val pendingComplaints = allComplaints.count { it.status.contains("Pending") || it.status.contains("Investigation") }

    GolfBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
        ) {
            item {
                GolfGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = GolfGoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Clubhouse Administration",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Admin master control for user registries, complaints vault, and pairings.",
                            fontSize = 12.sp,
                            color = GolfGoldLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Admin Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MetricBox(title = "Golfers", count = golfersCount.toString(), icon = Icons.Default.GolfCourse, modifier = Modifier.weight(1f))
                            MetricBox(title = "Caddies", count = caddiesCount.toString(), icon = Icons.Default.SportsScore, modifier = Modifier.weight(1f))
                            MetricBox(title = "Complaints", count = allComplaints.size.toString(), icon = Icons.Default.ReportProblem, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Quick Action: Manage Complaints Vault
            item {
                GolfGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Confidential Complaints Vault",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "$pendingComplaints incident reports require review",
                                fontSize = 11.sp,
                                color = if (pendingComplaints > 0) GolfGoldAccent else GolfGreenLight
                            )
                        }

                        Button(
                            onClick = onNavigateToComplaints,
                            colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Open Vault", color = Color(0xFF142B1B), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // User Management Header & Add Member Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MANAGE USERS (${allUsers.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GolfGoldAccent
                    )

                    Button(
                        onClick = { showAddMemberDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                        modifier = Modifier.testTag("admin_add_user_button")
                    ) {
                        Icon(Icons.Default.GroupAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Member", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(allUsers) { user ->
                GolfGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GolfGreenDark)
                                .border(1.dp, GolfGoldAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (user.role) {
                                    "golfer" -> Icons.Default.GolfCourse
                                    "caddie" -> Icons.Default.SportsScore
                                    else -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = null,
                                tint = GolfGoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GolfGoldAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${user.role.uppercase()} • ${user.id}",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GolfGoldAccent
                                    )
                                }
                            }
                            Text(
                                text = "${user.phoneNumber} • ${user.address}",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                maxLines = 1
                            )
                        }

                        if (user.role != "admin") {
                            IconButton(
                                onClick = { onDeleteUser(user.id) },
                                modifier = Modifier.testTag("admin_delete_user_${user.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove User",
                                    tint = Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Member Dialog
        if (showAddMemberDialog) {
            var newId by remember { mutableStateOf("G-${300 + allUsers.size}") }
            var newName by remember { mutableStateOf("") }
            var newRole by remember { mutableStateOf("golfer") }
            var newPhone by remember { mutableStateOf("+1 (555) 000-0000") }
            var newAddress by remember { mutableStateOf("Monterey Golf Club, CA") }
            var newExtra by remember { mutableStateOf("Handicap: 10") }

            AlertDialog(
                onDismissRequest = { showAddMemberDialog = false },
                containerColor = Color(0xFF132B1C),
                title = {
                    Text(
                        text = "Register New Member to Club",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GolfGoldAccent
                    )
                },
                text = {
                    Column {
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (newRole == "golfer") GolfGoldAccent else Color.Black.copy(alpha = 0.4f))
                                    .clickable {
                                        newRole = "golfer"
                                        newId = "G-${100 + allUsers.size}"
                                        newExtra = "Handicap: 10"
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text("Golfer", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (newRole == "golfer") Color(0xFF142B1B) else Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (newRole == "caddie") GolfGoldAccent else Color.Black.copy(alpha = 0.4f))
                                    .clickable {
                                        newRole = "caddie"
                                        newId = "C-${200 + allUsers.size}"
                                        newExtra = "Class A • 4 yrs exp"
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text("Caddie", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (newRole == "caddie") Color(0xFF142B1B) else Color.White)
                            }
                        }

                        OutlinedTextField(
                            value = newId,
                            onValueChange = { newId = it },
                            label = { Text("Login ID") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("admin_user_name_input")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text("Phone") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newAddress,
                            onValueChange = { newAddress = it },
                            label = { Text("Address") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = newExtra,
                            onValueChange = { newExtra = it },
                            label = { Text(if (newRole == "golfer") "Handicap" else "Experience") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newName.isNotBlank() && newId.isNotBlank()) {
                                onAddUser(newId, newName, newRole, newPhone, newAddress, newExtra)
                                showAddMemberDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                        modifier = Modifier.testTag("admin_confirm_add_user")
                    ) {
                        Text("Add Member", color = Color(0xFF142B1B), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showAddMemberDialog = false },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF091C0E).copy(alpha = 0.8f))
            .border(1.dp, GolfGoldAccent.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(count, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(title, fontSize = 10.sp, color = GolfGoldLight)
        }
    }
}
