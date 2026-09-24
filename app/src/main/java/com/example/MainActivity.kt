package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.GolfBannerAlert
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.ComplaintScreen
import com.example.ui.screens.DirectoryScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NoticeBoardScreen
import com.example.ui.screens.PrivateChartScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PublicChartScreen
import com.example.ui.theme.GolfDarkSurface
import com.example.ui.theme.GolfGoldAccent
import com.example.ui.theme.GolfGreenDark
import com.example.ui.theme.GolfGreenPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.GolfViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GolfApp()
            }
        }
    }
}

@Composable
fun GolfApp(viewModel: GolfViewModel = viewModel()) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val bannerMessage by viewModel.bannerMessage.collectAsStateWithLifecycle()

    val allGolfers by viewModel.allGolfers.collectAsStateWithLifecycle()
    val allCaddies by viewModel.allCaddies.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val dailyChart by viewModel.dailyChart.collectAsStateWithLifecycle()
    val allChartEntries by viewModel.allChartEntries.collectAsStateWithLifecycle()
    val userPlayActivity by viewModel.userPlayActivity.collectAsStateWithLifecycle()
    val recordedDates by viewModel.recordedDates.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val privateCharts by viewModel.privateCharts.collectAsStateWithLifecycle()
    val allComplaints by viewModel.allComplaints.collectAsStateWithLifecycle()
    val userComplaints by viewModel.userComplaints.collectAsStateWithLifecycle()
    val noticePosts by viewModel.noticePosts.collectAsStateWithLifecycle()
    val noticeReplies by viewModel.noticeReplies.collectAsStateWithLifecycle()

    val isAdmin = currentUser?.role == "admin"

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = GolfDarkSurface,
        bottomBar = {
            if (currentUser != null && currentDestination != AppDestination.LOGIN) {
                NavigationBar(
                    containerColor = Color(0xFF0A1B0F).copy(alpha = 0.95f),
                    contentColor = Color.White,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.PUBLIC_CHART,
                        onClick = { viewModel.navigateTo(AppDestination.PUBLIC_CHART) },
                        icon = { Icon(Icons.Default.TableChart, contentDescription = "Daily Chart", modifier = Modifier.size(20.dp)) },
                        label = { Text("Daily Chart", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_public_chart")
                    )

                    NavigationBarItem(
                        selected = currentDestination == AppDestination.PRIVATE_CHART,
                        onClick = { viewModel.navigateTo(AppDestination.PRIVATE_CHART) },
                        icon = { Icon(Icons.Default.Lock, contentDescription = "Private Chart", modifier = Modifier.size(20.dp)) },
                        label = { Text("Private", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_private_chart")
                    )

                    NavigationBarItem(
                        selected = currentDestination == AppDestination.DIRECTORY,
                        onClick = { viewModel.navigateTo(AppDestination.DIRECTORY) },
                        icon = { Icon(Icons.Default.People, contentDescription = "Directory", modifier = Modifier.size(20.dp)) },
                        label = { Text("Directory", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_directory")
                    )

                    NavigationBarItem(
                        selected = currentDestination == AppDestination.NOTICE_BOARD,
                        onClick = { viewModel.navigateTo(AppDestination.NOTICE_BOARD) },
                        icon = { Icon(Icons.Default.Forum, contentDescription = "Notice Board", modifier = Modifier.size(20.dp)) },
                        label = { Text("Notices", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_notice_board")
                    )

                    NavigationBarItem(
                        selected = currentDestination == AppDestination.COMPLAINT_ASSISTANT,
                        onClick = { viewModel.navigateTo(AppDestination.COMPLAINT_ASSISTANT) },
                        icon = {
                            Icon(
                                if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Security,
                                contentDescription = "Complaints",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text(if (isAdmin) "Vault" else "Complaints", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_complaints")
                    )

                    if (isAdmin) {
                        NavigationBarItem(
                            selected = currentDestination == AppDestination.ADMIN_PANEL,
                            onClick = { viewModel.navigateTo(AppDestination.ADMIN_PANEL) },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin Panel", modifier = Modifier.size(20.dp)) },
                            label = { Text("Admin", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0F2615),
                                selectedTextColor = GolfGoldAccent,
                                indicatorColor = GolfGoldAccent,
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                unselectedTextColor = Color.White.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.testTag("nav_admin")
                        )
                    }

                    NavigationBarItem(
                        selected = currentDestination == AppDestination.PROFILE,
                        onClick = { viewModel.navigateTo(AppDestination.PROFILE) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(20.dp)) },
                        label = { Text("Profile", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0F2615),
                            selectedTextColor = GolfGoldAccent,
                            indicatorColor = GolfGoldAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentDestination, label = "ScreenTransition") { destination ->
                when (destination) {
                    AppDestination.LOGIN -> {
                        LoginScreen(
                            golfers = allGolfers,
                            caddies = allCaddies,
                            onSelectUser = { viewModel.loginWithExistingUser(it) },
                            onRegisterOrLogin = { id, name, role, phone, address, extra ->
                                viewModel.loginOrRegister(id, name, role, phone, address, extra)
                            }
                        )
                    }
                    AppDestination.PUBLIC_CHART -> {
                        PublicChartScreen(
                            dailyChart = dailyChart,
                            todayDate = viewModel.todayDateFormatted,
                            todayDisplayDate = viewModel.todayDisplayDate,
                            selectedDate = selectedDate,
                            recordedDates = recordedDates,
                            searchQuery = searchQuery,
                            currentUser = currentUser,
                            allGolfers = allGolfers,
                            allCaddies = allCaddies,
                            onDateSelected = { viewModel.setSelectedDate(it) },
                            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                            onUpdateEntry = { entry, golferName, caddieName, caddieAadhaar, caddieNumber, phone, address, timeSlot, notes ->
                                viewModel.updateChartEntry(entry, golferName, caddieName, caddieAadhaar, caddieNumber, phone, address, timeSlot, notes)
                            },
                            onFinishRound = { viewModel.finishRound(it) },
                            onAddPairing = { golfer, caddie, caddieAadhaar, caddieNumber, timeSlot, notes ->
                                viewModel.addManualPairing(golfer, caddie, caddieAadhaar, caddieNumber, timeSlot, notes)
                            },
                            onRunAutoAssign = { viewModel.runAutoAssignForDate(it) }
                        )
                    }
                    AppDestination.PRIVATE_CHART -> {
                        PrivateChartScreen(
                            currentUser = currentUser,
                            privateCharts = privateCharts,
                            onAddPrivateEntry = { title, holes, score, caddieNotes, time, notes ->
                                viewModel.addPrivateChartEntry(title, holes, score, caddieNotes, time, notes)
                            },
                            onDeletePrivateEntry = { viewModel.deletePrivateChartEntry(it) }
                        )
                    }
                    AppDestination.DIRECTORY -> {
                        DirectoryScreen(
                            allGolfers = allGolfers,
                            allCaddies = allCaddies,
                            allDailyChartEntries = allChartEntries
                        )
                    }
                    AppDestination.NOTICE_BOARD -> {
                        NoticeBoardScreen(
                            currentUser = currentUser,
                            posts = noticePosts,
                            replies = noticeReplies,
                            onCreatePost = { title, content ->
                                viewModel.createNoticePost(title, content)
                            },
                            onReplyToPost = { postId, text ->
                                viewModel.createNoticeReply(postId, text)
                            }
                        )
                    }
                    AppDestination.COMPLAINT_ASSISTANT -> {
                        ComplaintScreen(
                            currentUser = currentUser,
                            allGolfers = allGolfers,
                            allCaddies = allCaddies,
                            userComplaints = userComplaints,
                            allComplaintsForAdmin = allComplaints,
                            onSubmitComplaint = { targetName, targetRole, category, details ->
                                viewModel.submitComplaint(targetName, targetRole, category, details)
                            },
                            onAdminUpdateStatus = { complaint, status, notes ->
                                viewModel.adminUpdateComplaint(complaint, status, notes)
                            }
                        )
                    }
                    AppDestination.PROFILE -> {
                        ProfileScreen(
                            currentUser = currentUser,
                            playActivity = userPlayActivity,
                            onUpdateProfile = { name, phone, address, extra ->
                                viewModel.updateProfile(name, phone, address, extra)
                            },
                            onLogout = { viewModel.logout() }
                        )
                    }
                    AppDestination.ADMIN_PANEL -> {
                        AdminPanelScreen(
                            allUsers = allUsers,
                            allComplaints = allComplaints,
                            onAddUser = { id, name, role, phone, address, extra ->
                                viewModel.adminAddUser(id, name, role, phone, address, extra)
                            },
                            onDeleteUser = { viewModel.adminDeleteUser(it) },
                            onNavigateToComplaints = { viewModel.navigateTo(AppDestination.COMPLAINT_ASSISTANT) }
                        )
                    }
                }
            }

            // Floating banner for confirmations
            if (bannerMessage != null) {
                GolfBannerAlert(
                    message = bannerMessage!!,
                    onDismiss = { viewModel.clearBanner() }
                )
            }
        }
    }
}
