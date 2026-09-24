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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsGolf
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

@Composable
fun DirectoryScreen(
    allGolfers: List<UserProfile>,
    allCaddies: List<UserProfile>,
    allDailyChartEntries: List<DailyChartEntry> = emptyList(),
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Golfers, 1: Caddies
    var directorySearch by remember { mutableStateOf("") }
    var selectedMemberForProfile by remember { mutableStateOf<UserProfile?>(null) }

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
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = GolfGoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Club Directory",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Public member roster & full play history",
                        fontSize = 12.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }

                Surface(
                    color = Color(0x551B5E20),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Text(
                        text = "${allGolfers.size} Golfers • ${allCaddies.size} Caddies",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search
            OutlinedTextField(
                value = directorySearch,
                onValueChange = { directorySearch = it },
                placeholder = { Text("Search public roster by name, ID, phone, or address...", color = Color(0x66FFFFFF), fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GolfGoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GolfGoldAccent,
                    unfocusedBorderColor = Color(0x4481C784)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("directory_search_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tab bar
            val tabs = listOf("Golfers (${allGolfers.size})", "Caddies (${allCaddies.size})")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0x770D2415),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GolfGoldAccent,
                        height = 2.5.dp
                    )
                }
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == idx) GolfGoldAccent else Color(0xCCFFFFFF),
                                fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.testTag("directory_tab_$idx")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            if (selectedTab == 0) {
                val filteredGolfers = allGolfers.filter {
                    directorySearch.isBlank() ||
                    it.name.contains(directorySearch, ignoreCase = true) ||
                    it.id.contains(directorySearch, ignoreCase = true) ||
                    it.phoneNumber.contains(directorySearch) ||
                    it.address.contains(directorySearch, ignoreCase = true)
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("golfers_directory_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredGolfers, key = { it.id }) { golfer ->
                        val latestRound = allDailyChartEntries.firstOrNull { it.golferId == golfer.id || it.golferName.equals(golfer.name, ignoreCase = true) }
                        DirectoryUserCard(
                            user = golfer,
                            assignedToday = latestRound?.caddieName,
                            onClick = { selectedMemberForProfile = golfer }
                        )
                    }
                }
            } else {
                val filteredCaddies = allCaddies.filter {
                    directorySearch.isBlank() ||
                    it.name.contains(directorySearch, ignoreCase = true) ||
                    it.id.contains(directorySearch, ignoreCase = true) ||
                    it.phoneNumber.contains(directorySearch) ||
                    it.address.contains(directorySearch, ignoreCase = true)
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("caddies_directory_list"),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCaddies, key = { it.id }) { caddie ->
                        val latestRound = allDailyChartEntries.firstOrNull { it.caddieId == caddie.id || it.caddieName.equals(caddie.name, ignoreCase = true) }
                        DirectoryUserCard(
                            user = caddie,
                            assignedToday = latestRound?.golferName,
                            onClick = { selectedMemberForProfile = caddie }
                        )
                    }
                }
            }
        }
    }

    // Public Profile Modal Dialog
    if (selectedMemberForProfile != null) {
        val member = selectedMemberForProfile!!
        val memberRounds = allDailyChartEntries.filter {
            if (member.role == "caddie") {
                it.caddieId == member.id || it.caddieName.equals(member.name, ignoreCase = true)
            } else {
                it.golferId == member.id || it.golferName.equals(member.name, ignoreCase = true)
            }
        }

        PublicMemberProfileDialog(
            user = member,
            rounds = memberRounds,
            onDismiss = { selectedMemberForProfile = null }
        )
    }
}

@Composable
fun DirectoryUserCard(
    user: UserProfile,
    assignedToday: String?,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xE60D2012)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0x3381C784)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("user_card_${user.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (user.role == "golfer") Color(0x332E7D32) else Color(0x33D4AF37))
                    .border(1.5.dp, if (user.role == "golfer") Color(0xFF81C784) else GolfGoldAccent, CircleShape),
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
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }

                if (user.experienceOrHandicap.isNotBlank()) {
                    Text(
                        text = user.experienceOrHandicap,
                        color = Color(0xFFA5D6A7),
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "${user.phoneNumber} • ${user.address}",
                    color = Color(0xAAFFFFFF),
                    fontSize = 11.sp,
                    maxLines = 1
                )

                if (assignedToday != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (user.role == "caddie") "Play with Golfer: $assignedToday" else "Paired Caddie: $assignedToday",
                        color = GolfGoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Public Profile badge
            Surface(
                color = Color(0x3381C784),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, Color(0x6681C784))
            ) {
                Text(
                    text = "View",
                    color = Color(0xFFC8E6C9),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PublicMemberProfileDialog(
    user: UserProfile,
    rounds: List<DailyChartEntry>,
    onDismiss: () -> Unit
) {
    val isCaddie = user.role == "caddie"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2012)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("public_member_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isCaddie) Color(0x33D4AF37) else Color(0x332E7D32))
                                .border(1.5.dp, if (isCaddie) GolfGoldAccent else Color(0xFF81C784), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCaddie) Icons.Default.Badge else Icons.Default.SportsGolf,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = user.name,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "ID: ${user.id} • ", color = GolfGoldAccent, fontSize = 11.sp)
                                Text(
                                    text = user.role.uppercase(),
                                    color = if (isCaddie) GolfGoldAccent else Color(0xFF81C784),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xAAFFFFFF))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Public Contact & Bio details
                Surface(
                    color = Color(0x5508170D),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0x3381C784)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Phone: ${user.phoneNumber.ifBlank { "Not provided" }}", color = Color.White, fontSize = 12.sp)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Address: ${user.address.ifBlank { "Not provided" }}", color = Color.White, fontSize = 12.sp)
                        }

                        if (user.experienceOrHandicap.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SportsGolf, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "${if (isCaddie) "Caddie Experience" else "Handicap"}: ${user.experienceOrHandicap}",
                                    color = Color(0xFFC8E6C9),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Public Play Activity Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = GolfGoldAccent, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCaddie) "Play with Golfer History" else "Rounds & Caddie History",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${rounds.size} Rounds",
                        color = Color(0xFFA5D6A7),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (rounds.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No recorded rounds for this member yet.", color = Color(0x88FFFFFF), fontSize = 12.sp)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        rounds.forEach { round ->
                            Surface(
                                color = Color(0x6608170D),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0x2281C784)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${round.date} • ${round.timeSlot.ifBlank { "Tee Time" }}",
                                            color = GolfGoldAccent,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (round.autoFinishTime.isNotBlank()) {
                                            Text(
                                                text = "Finished ${round.autoFinishTime}",
                                                color = Color(0xFF81C784),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    if (isCaddie) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Played with Golfer: ", color = Color(0xAAFFFFFF), fontSize = 12.sp)
                                            Text(
                                                text = round.golferName,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Round with Caddie: ", color = Color(0xAAFFFFFF), fontSize = 12.sp)
                                            Text(
                                                text = round.caddieName,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        val cadNum = if (round.caddieNumber.isNotBlank()) round.caddieNumber else round.caddieId
                                        if (cadNum.isNotBlank() || round.caddieAadhaar.isNotBlank()) {
                                            Text(
                                                text = "Caddie No: ${cadNum.ifBlank { "N/A" }} • Aadhaar: ${round.caddieAadhaar.ifBlank { "N/A" }}",
                                                color = GolfGoldAccent,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
