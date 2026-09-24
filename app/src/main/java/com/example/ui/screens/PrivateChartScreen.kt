package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PrivateChartEntry
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.theme.GolfGoldAccent

@Composable
fun PrivateChartScreen(
    currentUser: UserProfile?,
    privateCharts: List<PrivateChartEntry>,
    onAddPrivateEntry: (roundTitle: String, holes: String, score: String, caddieNotes: String, privateTime: String, personalNotes: String) -> Unit,
    onDeletePrivateEntry: (PrivateChartEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }

    GolfBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
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
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = GolfGoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Private Chart",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Personal confidential stats for ${currentUser?.name ?: "Member"}",
                        fontSize = 12.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_private_entry_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B241C), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ New Chart", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notice badge
            Surface(
                color = Color(0x77000000),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0x3381C784)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "All golfers and caddies can maintain private records visible only to their account.",
                        color = Color(0xEEFFFFFF),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // List of private records
            if (privateCharts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SportsScore, contentDescription = null, tint = Color(0x6681C784), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No private chart entries yet", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Tap '+ New Chart' to log private rounds, holes, or caddie notes.", color = Color(0xAAFFFFFF), fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("private_charts_list"),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(privateCharts, key = { it.id }) { entry ->
                        PrivateChartCard(
                            entry = entry,
                            onDelete = { onDeletePrivateEntry(entry) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddPrivateEntryDialog(
            userRole = currentUser?.role ?: "golfer",
            onDismiss = { showAddDialog = false },
            onAdd = { title, holes, score, caddieNotes, time, notes ->
                onAddPrivateEntry(title, holes, score, caddieNotes, time, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PrivateChartCard(
    entry: PrivateChartEntry,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xEE0B1E12)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0x4481C784)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("private_entry_${entry.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.roundTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Date: ${entry.date} • Time: ${entry.privateTime}",
                        fontSize = 11.sp,
                        color = GolfGoldAccent
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp).testTag("delete_private_${entry.id}")) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF8A80), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    color = Color(0x332E7D32),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Text(
                        text = "Holes: ${entry.holesOrCourse}",
                        color = Color(0xFFC8E6C9),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (entry.scoreOrPar.isNotBlank()) {
                    Surface(
                        color = Color(0x33D4AF37),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, GolfGoldAccent)
                    ) {
                        Text(
                            text = "Score: ${entry.scoreOrPar}",
                            color = GolfGoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            if (entry.caddieOrGolferNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Caddie / Partner Notes: ${entry.caddieOrGolferNotes}",
                    fontSize = 12.sp,
                    color = Color(0xFFA5D6A7)
                )
            }

            if (entry.personalNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Confidential Log: ${entry.personalNotes}",
                    fontSize = 12.sp,
                    color = Color(0xDDFFFFFF)
                )
            }
        }
    }
}

@Composable
fun AddPrivateEntryDialog(
    userRole: String,
    onDismiss: () -> Unit,
    onAdd: (title: String, holes: String, score: String, caddieNotes: String, time: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("Morning Round") }
    var holes by remember { mutableStateOf("18 Holes") }
    var score by remember { mutableStateOf("") }
    var caddieNotes by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("07:30 AM") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2615)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("New Private Chart Entry", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Log personal score, caddie notes, or round timing", fontSize = 11.sp, color = GolfGoldAccent)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Round Title / Description") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = holes,
                        onValueChange = { holes = it },
                        label = { Text("Holes") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = score,
                        onValueChange = { score = it },
                        label = { Text(if (userRole == "caddie") "Yardage" else "Score") },
                        placeholder = { Text(if (userRole == "caddie") "e.g. Accurate" else "e.g. 74 (+2)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Round Time / Timing Slot") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = caddieNotes,
                    onValueChange = { caddieNotes = it },
                    label = { Text(if (userRole == "caddie") "Golfer Play Notes" else "Caddie Performance Notes") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Personal Confidential Log") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = Color(0xAAFFFFFF)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onAdd(title.trim(), holes.trim(), score.trim(), caddieNotes.trim(), time.trim(), notes.trim())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Save Record", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
