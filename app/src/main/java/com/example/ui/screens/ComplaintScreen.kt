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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
fun ComplaintScreen(
    currentUser: UserProfile?,
    allGolfers: List<UserProfile>,
    allCaddies: List<UserProfile>,
    userComplaints: List<Complaint>,
    allComplaintsForAdmin: List<Complaint>,
    onSubmitComplaint: (targetName: String, targetRole: String, category: String, details: String) -> Unit,
    onAdminUpdateStatus: (Complaint, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAdmin = currentUser?.role == "admin"
    var showAssistantDialog by remember { mutableStateOf(false) }
    var selectedComplaintForReview by remember { mutableStateOf<Complaint?>(null) }

    GolfBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
        ) {
            // Header Card
            item {
                GolfGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Security,
                                        contentDescription = null,
                                        tint = GolfGoldAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAdmin) "Admin Incident Vault" else "Confidential Complaint Box",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = if (isAdmin) "Review and resolve filed golfer & caddie reports" else "Guided Assistant • Visible ONLY to Club Admin",
                                    fontSize = 12.sp,
                                    color = GolfGoldLight
                                )
                            }

                            if (!isAdmin) {
                                Button(
                                    onClick = { showAssistantDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("open_assistant_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Assistant,
                                        contentDescription = null,
                                        tint = Color(0xFF142B1B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Assistant",
                                        color = Color(0xFF142B1B),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Confidentiality Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = GolfGoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Strict Privacy Protocol: Reports filed in this box are completely sealed from public view and delivered directly to the club admin.",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // If Admin: View all complaints
            if (isAdmin) {
                item {
                    Text(
                        text = "ALL FILED COMPLAINTS (${allComplaintsForAdmin.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GolfGoldAccent,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                if (allComplaintsForAdmin.isEmpty()) {
                    item {
                        GolfGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                            Text(
                                text = "No complaints filed yet. Course etiquette remains exemplary!",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }

                items(allComplaintsForAdmin) { complaint ->
                    ComplaintItemCard(
                        complaint = complaint,
                        isAdmin = true,
                        onReview = { selectedComplaintForReview = complaint }
                    )
                }
            } else {
                // Regular Golfer or Caddie view: Show assistant trigger banner & their own submissions
                item {
                    GolfGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(GolfGreenPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Assistant,
                                        contentDescription = null,
                                        tint = GolfGoldAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Need to Report an Incident?",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "The Assistant will help you structure a discreet complaint.",
                                        fontSize = 11.sp,
                                        color = GolfGoldLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { showAssistantDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = GolfGreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("launch_complaint_assistant")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("File Complaint with Assistant", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "YOUR SUBMITTED REPORTS (${userComplaints.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GolfGoldAccent,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                if (userComplaints.isEmpty()) {
                    item {
                        GolfGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                            Text(
                                text = "You have not submitted any complaints.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }

                items(userComplaints) { complaint ->
                    ComplaintItemCard(
                        complaint = complaint,
                        isAdmin = false,
                        onReview = {}
                    )
                }
            }
        }

        // Assistant Guided Complaint Dialog
        if (showAssistantDialog) {
            var selectedTargetRole by remember { mutableStateOf("caddie") }
            var targetNameInput by remember { mutableStateOf("") }
            var categoryInput by remember { mutableStateOf("Conduct / Etiquette") }
            var descriptionInput by remember { mutableStateOf("") }

            val categories = listOf(
                "Conduct / Etiquette",
                "Punctuality / No Show",
                "Equipment Misuse",
                "Score Dispute",
                "Pace of Play",
                "Safety Violation",
                "Other"
            )

            AlertDialog(
                onDismissRequest = { showAssistantDialog = false },
                containerColor = Color(0xFF132B1B),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assistant, contentDescription = null, tint = GolfGoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Assistant: File Confidential Complaint",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GolfGoldAccent
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Step 1: Who is this complaint against?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Row(modifier = Modifier.padding(vertical = 6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedTargetRole == "caddie") GolfGoldAccent else Color.Black.copy(alpha = 0.4f))
                                    .clickable { selectedTargetRole = "caddie" }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    "A Caddie",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTargetRole == "caddie") Color(0xFF142B1B) else Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedTargetRole == "golfer") GolfGoldAccent else Color.Black.copy(alpha = 0.4f))
                                    .clickable { selectedTargetRole = "golfer" }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    "A Golfer",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTargetRole == "golfer") Color(0xFF142B1B) else Color.White
                                )
                            }
                        }

                        // Quick selector chips for names
                        val candidateList = if (selectedTargetRole == "caddie") allCaddies else allGolfers
                        Text(
                            text = "Choose from roster or type below:",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            items(candidateList) { candidate ->
                                val isSelected = targetNameInput == candidate.name
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) GolfGreenPrimary else Color.White.copy(alpha = 0.15f))
                                        .clickable { targetNameInput = candidate.name }
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        candidate.name,
                                        fontSize = 10.sp,
                                        color = if (isSelected) GolfGoldAccent else Color.White
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = targetNameInput,
                            onValueChange = { targetNameInput = it },
                            label = { Text("Person's Name") },
                            placeholder = { Text("e.g. John Sullivan") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("target_person_name_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Step 2: What is the category of the incident?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            items(categories) { cat ->
                                val isSelected = cat == categoryInput
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) GolfGreenPrimary else Color.Black.copy(alpha = 0.4f))
                                        .clickable { categoryInput = cat }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        cat,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) GolfGoldAccent else Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Step 3: Assistant Incident Description",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            placeholder = { Text("Please describe what happened, hole location, timing, and details...") },
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GolfGoldAccent,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("complaint_description_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (targetNameInput.isNotBlank() && descriptionInput.isNotBlank()) {
                                onSubmitComplaint(targetNameInput, selectedTargetRole, categoryInput, descriptionInput)
                                showAssistantDialog = false
                            }
                        },
                        enabled = targetNameInput.isNotBlank() && descriptionInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                        modifier = Modifier.testTag("submit_complaint_button")
                    ) {
                        Text("File to Admin Vault", color = Color(0xFF142B1B), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showAssistantDialog = false },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Admin Status & Resolution Dialog
        if (selectedComplaintForReview != null) {
            val c = selectedComplaintForReview!!
            var statusChoice by remember(c) { mutableStateOf(c.status) }
            var notesInput by remember(c) { mutableStateOf(c.adminResolutionNotes) }

            val statusOptions = listOf("Under Investigation", "Resolved", "Dismissed")

            AlertDialog(
                onDismissRequest = { selectedComplaintForReview = null },
                containerColor = Color(0xFF132B1C),
                title = {
                    Text(
                        text = "Admin Resolution • Report #${c.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GolfGoldAccent
                    )
                },
                text = {
                    Column {
                        Text("Complainant: ${c.filedByName} (${c.filedByRole})", fontSize = 12.sp, color = Color.White)
                        Text("Against: ${c.targetPersonName} (${c.targetPersonRole})", fontSize = 12.sp, color = GolfGoldLight, fontWeight = FontWeight.Bold)
                        Text("Category: ${c.category}", fontSize = 11.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Statement: \"${c.description}\"", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Update Investigation Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GolfGoldAccent)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            statusOptions.forEach { opt ->
                                val isSelected = opt == statusChoice
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) GolfGoldAccent else Color.Black.copy(alpha = 0.4f))
                                        .clickable { statusChoice = opt }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        opt,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFF142B1B) else Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = notesInput,
                            onValueChange = { notesInput = it },
                            label = { Text("Admin Official Resolution Log") },
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
                            onAdminUpdateStatus(c, statusChoice, notesInput)
                            selectedComplaintForReview = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Save Status", color = Color(0xFF142B1B), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { selectedComplaintForReview = null },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
private fun ComplaintItemCard(
    complaint: Complaint,
    isAdmin: Boolean,
    onReview: () -> Unit
) {
    GolfGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Report against ${complaint.targetPersonName}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GolfGreenDark)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = complaint.targetPersonRole.uppercase(),
                                fontSize = 8.sp,
                                color = GolfGoldAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Category: ${complaint.category} • ${complaint.dateFiled}",
                        fontSize = 11.sp,
                        color = GolfGoldLight
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (complaint.status) {
                                "Resolved" -> GolfGreenPrimary
                                "Under Investigation" -> Color(0xFFD35400)
                                else -> Color(0xFF7F8C8D)
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = complaint.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = complaint.description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            if (isAdmin) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.White.copy(alpha = 0.1f), thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filed by: ${complaint.filedByName} (${complaint.filedByRole})",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Button(
                        onClick = onReview,
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Admin Action", fontSize = 10.sp, color = Color(0xFF142B1B), fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (complaint.adminResolutionNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0C2414).copy(alpha = 0.8f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Admin Log: ${complaint.adminResolutionNotes}",
                        fontSize = 10.sp,
                        color = GolfGreenLight
                    )
                }
            }
        }
    }
}
