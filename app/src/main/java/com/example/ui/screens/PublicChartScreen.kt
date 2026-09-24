package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DailyChartEntry
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.theme.GolfGoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicChartScreen(
    dailyChart: List<DailyChartEntry>,
    todayDate: String,
    todayDisplayDate: String,
    selectedDate: String,
    recordedDates: List<String>,
    searchQuery: String,
    currentUser: UserProfile?,
    allGolfers: List<UserProfile>,
    allCaddies: List<UserProfile>,
    onDateSelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onUpdateEntry: (
        entry: DailyChartEntry,
        golferName: String,
        caddieName: String,
        caddieAadhaar: String,
        caddieNumber: String,
        phone: String,
        address: String,
        timeSlot: String,
        notes: String
    ) -> Unit,
    onFinishRound: (DailyChartEntry) -> Unit,
    onAddPairing: (
        golfer: UserProfile,
        caddie: UserProfile,
        caddieAadhaar: String,
        caddieNumber: String,
        timeSlot: String,
        notes: String
    ) -> Unit,
    onRunAutoAssign: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = (selectedDate == todayDate)
    var showAddDialog by remember { mutableStateOf(false) }
    var entryToEdit by remember { mutableStateOf<DailyChartEntry?>(null) }
    var showDatePickerDropdown by remember { mutableStateOf(false) }

    GolfBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Caddie's Day",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Daily Entry Chart • One Day One Chart System",
                        fontSize = 12.sp,
                        color = GolfGoldAccent,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Date Selector Chip
                Box {
                    Surface(
                        color = if (isToday) Color(0xD91E462B) else Color(0xD942251B),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isToday) GolfGoldAccent else Color(0xFFFFAB91)),
                        modifier = Modifier
                            .clickable { showDatePickerDropdown = true }
                            .testTag("date_selector_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = if (isToday) GolfGoldAccent else Color(0xFFFFAB91),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (isToday) "Today ($selectedDate)" else selectedDate,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isToday) "Live Present Edit" else "Archived • Read Only",
                                    color = if (isToday) Color(0xFFA5D6A7) else Color(0xFFFFAB91),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = showDatePickerDropdown,
                        onDismissRequest = { showDatePickerDropdown = false },
                        modifier = Modifier.background(Color(0xF00F2615))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Today ($todayDate) - Active", color = GolfGoldAccent, fontWeight = FontWeight.Bold)
                            },
                            onClick = {
                                onDateSelected(todayDate)
                                showDatePickerDropdown = false
                            }
                        )
                        recordedDates.filter { it != todayDate }.forEach { pastDate ->
                            DropdownMenuItem(
                                text = {
                                    Text("Past: $pastDate (Locked/Read Only)", color = Color.White)
                                },
                                onClick = {
                                    onDateSelected(pastDate)
                                    showDatePickerDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        text = "Search golfer, caddie, or Aadhaar in today's chart...",
                        color = Color(0x99FFFFFF),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GolfGoldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0x990D2415),
                    unfocusedContainerColor = Color(0x880D2415),
                    focusedBorderColor = GolfGoldAccent,
                    unfocusedBorderColor = Color(0x4481C784)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chart_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action row for Today: Auto-Assign & Add Pairing
            if (isToday) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onRunAutoAssign(todayDate) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xCC1B5E20)),
                        border = BorderStroke(1.dp, Color(0xFF81C784)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("auto_pair_btn")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Auto-Assign Caddies", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        border = BorderStroke(1.dp, GolfGoldAccent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("manual_add_pairing_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Add Entry", color = GolfGoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Surface(
                    color = Color(0x664E342E),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFAB91)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFFAB91), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Historical Archive ($selectedDate): Read Only. Records cannot be edited or deleted.",
                            color = Color(0xFFFFCCBC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chart Table Summary Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(Color(0xEE091B0E))
                    .border(1.dp, Color(0x3381C784), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "COLUMNS: S.No • Golfer • Caddie • Phone • Address • Time • Aadhaar",
                    color = GolfGoldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${dailyChart.size} Total Entries",
                    color = Color(0xFFA5D6A7),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Entries List
            if (dailyChart.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.GolfCourse, contentDescription = null, tint = Color(0x8881C784), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No chart entries found for $selectedDate", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        if (isToday) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Tap 'Auto-Assign Caddies' above to generate today's chart!", color = Color(0xAAFFFFFF), fontSize = 12.sp)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("chart_entries_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dailyChart, key = { it.id }) { entry ->
                        DailyChartCard(
                            entry = entry,
                            isToday = isToday,
                            onEditClick = { entryToEdit = entry },
                            onFinishClick = { onFinishRound(entry) }
                        )
                    }
                }
            }
        }
    }

    // Add Pairing Dialog
    if (showAddDialog) {
        ManualAddPairingDialog(
            golfers = allGolfers,
            caddies = allCaddies,
            onDismiss = { showAddDialog = false },
            onAdd = { g, c, aadhaar, cadNum, time, notes ->
                onAddPairing(g, c, aadhaar, cadNum, time, notes)
                showAddDialog = false
            }
        )
    }

    // Edit Pairing Dialog
    if (entryToEdit != null && isToday) {
        EditChartEntryDialog(
            entry = entryToEdit!!,
            onDismiss = { entryToEdit = null },
            onSave = { golferName, caddieName, aadhaar, cadNum, phone, addr, time, notes ->
                onUpdateEntry(entryToEdit!!, golferName, caddieName, aadhaar, cadNum, phone, addr, time, notes)
                entryToEdit = null
            }
        )
    }
}

@Composable
fun DailyChartCard(
    entry: DailyChartEntry,
    isToday: Boolean,
    onEditClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val isCompleted = entry.autoFinishTime.isNotBlank()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xEE0E2415)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isCompleted) Color(0x6681C784) else Color(0x3381C784)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chart_entry_${entry.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Row: Serial No + Time + Status + Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0x33D4AF37),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, GolfGoldAccent)
                    ) {
                        Text(
                            text = "Serial No. #${entry.serialNumber}",
                            color = GolfGoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFFA5D6A7), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = entry.timeSlot.ifBlank { "08:00 AM" },
                            color = Color(0xFFA5D6A7),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isCompleted) {
                        Surface(
                            color = Color(0x332E7D32),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFF81C784))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Fin: ${entry.autoFinishTime}",
                                    color = Color(0xFF81C784),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else if (isToday) {
                        Button(
                            onClick = onFinishClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x3381C784)),
                            border = BorderStroke(1.dp, Color(0xFF81C784)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .height(26.dp)
                                .testTag("finish_btn_${entry.id}")
                        ) {
                            Text("Complete", fontSize = 10.sp, color = Color(0xFFC8E6C9), fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isToday) {
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier
                                .size(26.dp)
                                .testTag("edit_entry_${entry.id}")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit in Present Time", tint = GolfGoldAccent, modifier = Modifier.size(15.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Golfer & Caddie Pair Details Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x5508170D), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Golfer Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GOLFER NAME",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GolfGoldAccent
                    )
                    Text(
                        text = entry.golferName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (entry.golferId.isNotBlank()) {
                        Text(
                            text = "Golfer ID: ${entry.golferId}",
                            fontSize = 10.sp,
                            color = Color(0xAAFFFFFF)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Caddie Column
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CADDIE NAME",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF81C784)
                    )
                    Text(
                        text = entry.caddieName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    val cadIdOrNum = if (entry.caddieNumber.isNotBlank()) entry.caddieNumber else entry.caddieId
                    if (cadIdOrNum.isNotBlank()) {
                        Text(
                            text = "Caddie No: $cadIdOrNum",
                            fontSize = 10.sp,
                            color = Color(0xFFA5D6A7)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Phone, Address & Aadhaar Information (Mandated Columns)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xAAFFFFFF), modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Phone: ${entry.phoneNumber.ifBlank { "N/A" }}",
                        color = Color(0xCCFFFFFF),
                        fontSize = 11.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xAAFFFFFF), modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Address: ${entry.address.ifBlank { "N/A" }}",
                        color = Color(0xCCFFFFFF),
                        fontSize = 11.sp
                    )
                }

                if (entry.caddieAadhaar.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Aadhaar No: ${entry.caddieAadhaar}",
                            color = GolfGoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (entry.notes.isNotBlank()) {
                    Text(
                        text = "Notes: ${entry.notes}",
                        color = Color(0xFFC8E6C9),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ManualAddPairingDialog(
    golfers: List<UserProfile>,
    caddies: List<UserProfile>,
    onDismiss: () -> Unit,
    onAdd: (golfer: UserProfile, caddie: UserProfile, aadhaar: String, cadNum: String, timeSlot: String, notes: String) -> Unit
) {
    var selectedGolferIndex by remember { mutableStateOf(0) }
    var selectedCaddieIndex by remember { mutableStateOf(0) }
    var caddieAadhaar by remember { mutableStateOf("") }
    var caddieNumber by remember { mutableStateOf("") }
    var timeSlot by remember { mutableStateOf("08:00 AM") }
    var notes by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2615)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Add Daily Entry", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Pair golfer with an available caddie for today's chart", fontSize = 11.sp, color = GolfGoldAccent)
                Spacer(modifier = Modifier.height(12.dp))

                if (golfers.isNotEmpty()) {
                    Text("Select Golfer:", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = golfers[selectedGolferIndex.coerceIn(0, golfers.size - 1)].name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            TextButton(onClick = {
                                selectedGolferIndex = (selectedGolferIndex + 1) % golfers.size
                            }) {
                                Text("Next (${selectedGolferIndex + 1}/${golfers.size})", color = GolfGoldAccent, fontSize = 11.sp)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x66FFFFFF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (caddies.isNotEmpty()) {
                    Text("Select Caddie:", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = caddies[selectedCaddieIndex.coerceIn(0, caddies.size - 1)].name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            TextButton(onClick = {
                                selectedCaddieIndex = (selectedCaddieIndex + 1) % caddies.size
                            }) {
                                Text("Next (${selectedCaddieIndex + 1}/${caddies.size})", color = GolfGoldAccent, fontSize = 11.sp)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x66FFFFFF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = caddieNumber,
                    onValueChange = { caddieNumber = it },
                    label = { Text("Caddie Number (e.g. CAD-#101)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent,
                        unfocusedBorderColor = Color(0x66FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = caddieAadhaar,
                    onValueChange = { caddieAadhaar = it },
                    label = { Text("Aadhaar Number (e.g. XXXX-XXXX-4581)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent,
                        unfocusedBorderColor = Color(0x66FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Tee Time (e.g. 08:00 AM)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent,
                        unfocusedBorderColor = Color(0x66FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent,
                        unfocusedBorderColor = Color(0x66FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = error!!, color = Color(0xFFFF8A80), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xAAFFFFFF))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (golfers.isEmpty() || caddies.isEmpty()) {
                                error = "Need registered golfers and caddies"
                            } else {
                                onAdd(
                                    golfers[selectedGolferIndex.coerceIn(0, golfers.size - 1)],
                                    caddies[selectedCaddieIndex.coerceIn(0, caddies.size - 1)],
                                    caddieAadhaar.trim(),
                                    caddieNumber.trim(),
                                    timeSlot.trim(),
                                    notes.trim()
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Add Entry", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EditChartEntryDialog(
    entry: DailyChartEntry,
    onDismiss: () -> Unit,
    onSave: (
        golferName: String,
        caddieName: String,
        caddieAadhaar: String,
        caddieNumber: String,
        phone: String,
        addr: String,
        time: String,
        notes: String
    ) -> Unit
) {
    var golferName by remember { mutableStateOf(entry.golferName) }
    var caddieName by remember { mutableStateOf(entry.caddieName) }
    var caddieAadhaar by remember { mutableStateOf(entry.caddieAadhaar) }
    var caddieNumber by remember { mutableStateOf(entry.caddieNumber) }
    var phone by remember { mutableStateOf(entry.phoneNumber) }
    var address by remember { mutableStateOf(entry.address) }
    var timeSlot by remember { mutableStateOf(entry.timeSlot) }
    var notes by remember { mutableStateOf(entry.notes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2615)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Edit Present Time Chart Entry #${entry.serialNumber}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "All fields can be updated in present time (today's chart)",
                    fontSize = 11.sp,
                    color = GolfGoldAccent
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Golfer Name
                OutlinedTextField(
                    value = golferName,
                    onValueChange = { golferName = it },
                    label = { Text("Golfer Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Caddie Name
                OutlinedTextField(
                    value = caddieName,
                    onValueChange = { caddieName = it },
                    label = { Text("Caddie Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Caddie Number
                OutlinedTextField(
                    value = caddieNumber,
                    onValueChange = { caddieNumber = it },
                    label = { Text("Caddie Number (e.g. CAD-#101)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Aadhaar Number
                OutlinedTextField(
                    value = caddieAadhaar,
                    onValueChange = { caddieAadhaar = it },
                    label = { Text("Aadhaar Number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Time Slot
                OutlinedTextField(
                    value = timeSlot,
                    onValueChange = { timeSlot = it },
                    label = { Text("Tee Time (e.g. 08:30 AM)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
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
                            onSave(
                                golferName.trim(),
                                caddieName.trim(),
                                caddieAadhaar.trim(),
                                caddieNumber.trim(),
                                phone.trim(),
                                address.trim(),
                                timeSlot.trim(),
                                notes.trim()
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Save Changes", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
