package com.example.data.repository

import com.example.data.dao.ComplaintDao
import com.example.data.dao.DailyChartDao
import com.example.data.dao.NoticeBoardDao
import com.example.data.dao.PrivateChartDao
import com.example.data.dao.UserDao
import com.example.data.model.Complaint
import com.example.data.model.DailyChartEntry
import com.example.data.model.NoticePost
import com.example.data.model.NoticeReply
import com.example.data.model.PrivateChartEntry
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GolfRepository(
    private val userDao: UserDao,
    private val dailyChartDao: DailyChartDao,
    private val privateChartDao: PrivateChartDao,
    private val complaintDao: ComplaintDao,
    private val noticeBoardDao: NoticeBoardDao
) {
    // Flow sources
    val allUsers: Flow<List<UserProfile>> = userDao.getAllUsers()
    val allGolfers: Flow<List<UserProfile>> = userDao.getAllGolfers()
    val allCaddies: Flow<List<UserProfile>> = userDao.getAllCaddies()
    val allComplaints: Flow<List<Complaint>> = complaintDao.getAllComplaints()
    val allNoticePosts: Flow<List<NoticePost>> = noticeBoardDao.getAllPosts()
    val allNoticeReplies: Flow<List<NoticeReply>> = noticeBoardDao.getAllReplies()
    val recordedDates: Flow<List<String>> = dailyChartDao.getAllRecordedDates()

    fun getDailyChart(date: String): Flow<List<DailyChartEntry>> = dailyChartDao.getChartForDate(date)

    fun getEntriesForUser(userId: String, name: String): Flow<List<DailyChartEntry>> =
        dailyChartDao.getEntriesForUser(userId, name)

    fun getAllChartEntries(): Flow<List<DailyChartEntry>> = dailyChartDao.getAllChartEntries()

    fun searchDailyChart(date: String, query: String): Flow<List<DailyChartEntry>> =
        dailyChartDao.searchChart(date, query)

    fun getPrivateCharts(userId: String): Flow<List<PrivateChartEntry>> =
        privateChartDao.getPrivateChartsForUser(userId)

    fun getUserComplaints(userId: String): Flow<List<Complaint>> =
        complaintDao.getComplaintsFiledByUser(userId)

    fun getRepliesForPost(postId: Long): Flow<List<NoticeReply>> =
        noticeBoardDao.getRepliesForPost(postId)

    // Operations
    suspend fun saveUser(user: UserProfile) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserProfile) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(userId: String) {
        userDao.deleteUser(userId)
    }

    suspend fun getUserById(id: String): UserProfile? = userDao.getUserById(id)

    /**
     * Daily Random Caddie Assignment Algorithm:
     * 1. Checks if chart entries already exist for the requested date.
     * 2. If not, fetches all registered golfers and caddies.
     * 3. Shuffles available caddies randomly.
     * 4. Serially pairs each golfer with a distinct available caddie.
     * 5. Ensures once assigned, a caddie cannot be assigned to another golfer on that day (1-to-1 constraint).
     * 6. Inserts the entries into the database permanently (no delete option).
     */
    suspend fun generateDailyChartIfNotExists(date: String) {
        val existingCount = dailyChartDao.countEntriesForDate(date)
        if (existingCount > 0) return

        val golfers = userDao.getAllGolfers().first()
        val caddies = userDao.getAllCaddies().first().filter { it.isAvailable }

        if (golfers.isEmpty()) return

        val shuffledCaddies = caddies.shuffled().toMutableList()
        val assignedCaddieIds = mutableSetOf<String>()
        val newEntries = mutableListOf<DailyChartEntry>()

        val baseTimeSlots = listOf(
            "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM",
            "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM",
            "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM"
        )

        golfers.forEachIndexed { index, golfer ->
            val slotTime = baseTimeSlots.getOrElse(index) { "10:30 AM" }
            val caddie = shuffledCaddies.firstOrNull { it.id !in assignedCaddieIds }

            if (caddie != null) {
                assignedCaddieIds.add(caddie.id)
                shuffledCaddies.remove(caddie)
                val caddieNum = caddie.id.replace("C-", "CAD-#")
                val aadhaarSample = "XXXX-XXXX-${(4120 + (index * 143) % 5000)}"
                newEntries.add(
                    DailyChartEntry(
                        date = date,
                        serialNumber = index + 1,
                        golferId = golfer.id,
                        golferName = golfer.name,
                        caddieId = caddie.id,
                        caddieName = caddie.name,
                        caddieAadhaar = aadhaarSample,
                        caddieNumber = caddieNum,
                        phoneNumber = golfer.phoneNumber,
                        address = golfer.address,
                        timeSlot = slotTime,
                        autoFinishTime = "",
                        status = "Active",
                        notes = "Hole 1 Tee-off, 18 Holes"
                    )
                )
            } else {
                newEntries.add(
                    DailyChartEntry(
                        date = date,
                        serialNumber = index + 1,
                        golferId = golfer.id,
                        golferName = golfer.name,
                        caddieId = "",
                        caddieName = "Pending Caddie Availability",
                        caddieAadhaar = "N/A",
                        caddieNumber = "N/A",
                        phoneNumber = golfer.phoneNumber,
                        address = golfer.address,
                        timeSlot = slotTime,
                        autoFinishTime = "",
                        status = "Waitlisted",
                        notes = "Waiting for next available caddie"
                    )
                )
            }
        }

        if (newEntries.isNotEmpty()) {
            dailyChartDao.insertEntries(newEntries)
        }
    }

    suspend fun getAvailableCaddiesForDate(date: String): List<UserProfile> {
        val assignedIds = dailyChartDao.getAssignedCaddieIdsForDate(date).toSet()
        val allCaddiesList = userDao.getAllCaddies().first()
        return allCaddiesList.filter { it.id !in assignedIds && it.isAvailable }
    }

    suspend fun addChartEntry(entry: DailyChartEntry) {
        dailyChartDao.insertEntry(entry)
    }

    suspend fun updateChartEntry(entry: DailyChartEntry) {
        dailyChartDao.updateEntry(entry)
    }

    suspend fun markRoundFinished(entry: DailyChartEntry) {
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val updated = entry.copy(
            autoFinishTime = currentTime,
            status = "Completed"
        )
        dailyChartDao.updateEntry(updated)
    }

    suspend fun addPrivateChart(entry: PrivateChartEntry) {
        privateChartDao.insertPrivateEntry(entry)
    }

    suspend fun updatePrivateChart(entry: PrivateChartEntry) {
        privateChartDao.updatePrivateEntry(entry)
    }

    suspend fun deletePrivateChart(entry: PrivateChartEntry) {
        privateChartDao.deletePrivateEntry(entry)
    }

    suspend fun fileComplaint(complaint: Complaint) {
        complaintDao.insertComplaint(complaint)
    }

    suspend fun updateComplaint(complaint: Complaint) {
        complaintDao.updateComplaint(complaint)
    }

    suspend fun deleteComplaint(id: Long) {
        complaintDao.deleteComplaint(id)
    }

    suspend fun postNotice(post: NoticePost): Long {
        return noticeBoardDao.insertPost(post)
    }

    suspend fun replyToNotice(reply: NoticeReply): Long {
        return noticeBoardDao.insertReply(reply)
    }

    // Seed initial data if empty
    suspend fun seedInitialDataIfEmpty(todayDate: String) {
        val existingUsers = userDao.getAllUsers().first()
        if (existingUsers.isEmpty()) {
            val initialUsers = listOf(
                // Golfers
                UserProfile(
                    id = "G-101",
                    name = "Robert Palmer",
                    role = "golfer",
                    phoneNumber = "+1 (555) 234-5678",
                    address = "45 Fairway Blvd, Pebble Beach, CA",
                    experienceOrHandicap = "Handicap: 4.2",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "G-102",
                    name = "Thomas Sterling",
                    role = "golfer",
                    phoneNumber = "+1 (555) 345-6789",
                    address = "12 Cypress Point Rd, Carmel, CA",
                    experienceOrHandicap = "Handicap: 8.5",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "G-103",
                    name = "Victoria Vance",
                    role = "golfer",
                    phoneNumber = "+1 (555) 456-7890",
                    address = "88 Pinehurst Way, Monterey, CA",
                    experienceOrHandicap = "Handicap: 12.0",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "G-104",
                    name = "Marcus Holloway",
                    role = "golfer",
                    phoneNumber = "+1 (555) 567-8901",
                    address = "210 Augusta Dr, Pacific Grove, CA",
                    experienceOrHandicap = "Handicap: 6.8",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "G-105",
                    name = "Sophia Chen",
                    role = "golfer",
                    phoneNumber = "+1 (555) 678-9012",
                    address = "704 Links Ave, Seaside, CA",
                    experienceOrHandicap = "Handicap: 15.4",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "G-106",
                    name = "Arthur Pendelton",
                    role = "golfer",
                    phoneNumber = "+1 (555) 789-0123",
                    address = "19 Country Club Ln, Monterey, CA",
                    experienceOrHandicap = "Handicap: 9.1",
                    registeredDate = todayDate
                ),

                // Caddies
                UserProfile(
                    id = "C-201",
                    name = "John 'Bones' Sullivan",
                    role = "caddie",
                    phoneNumber = "+1 (555) 890-1234",
                    address = "112 Bunkers End, Monterey, CA",
                    experienceOrHandicap = "Class A Caddie • 8 yrs exp",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "C-202",
                    name = "Liam O'Connor",
                    role = "caddie",
                    phoneNumber = "+1 (555) 901-2345",
                    address = "402 Greenkeeper Rd, Marina, CA",
                    experienceOrHandicap = "Senior Caddie • 6 yrs exp",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "C-203",
                    name = "Carlos Ramirez",
                    role = "caddie",
                    phoneNumber = "+1 (555) 012-3456",
                    address = "55 Dunes View, Seaside, CA",
                    experienceOrHandicap = "Master Caddie • 10 yrs exp",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "C-204",
                    name = "David Miller",
                    role = "caddie",
                    phoneNumber = "+1 (555) 123-4567",
                    address = "87 Clubhouse Way, Pacific Grove, CA",
                    experienceOrHandicap = "Tour Caddie • 7 yrs exp",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "C-205",
                    name = "Ethan Brooks",
                    role = "caddie",
                    phoneNumber = "+1 (555) 234-6789",
                    address = "33 Sandridge Ct, Carmel, CA",
                    experienceOrHandicap = "Junior Caddie • 3 yrs exp",
                    registeredDate = todayDate
                ),
                UserProfile(
                    id = "C-206",
                    name = "Samuel Jackson",
                    role = "caddie",
                    phoneNumber = "+1 (555) 345-7890",
                    address = "90 Rough Cut Ln, Salinas, CA",
                    experienceOrHandicap = "Class A Caddie • 5 yrs exp",
                    registeredDate = todayDate
                ),

                // Admin
                UserProfile(
                    id = "ADMIN-01",
                    name = "Director William Scott",
                    role = "admin",
                    phoneNumber = "+1 (555) 111-2233",
                    address = "Clubhouse Suite 1, Pebble Beach, CA",
                    experienceOrHandicap = "Caddie Master & Head Marshal",
                    registeredDate = todayDate
                )
            )
            userDao.insertUsers(initialUsers)

            // Seed initial Notice Board post
            noticeBoardDao.insertPost(
                NoticePost(
                    authorId = "ADMIN-01",
                    authorName = "Director William Scott",
                    authorRole = "admin",
                    title = "Course Condition: Greens Aerated on Hole 7 & 12",
                    message = "Good morning golfers and caddies! Wind speed is 8 knots SW today. Pin placements are Championship Position B. Please make sure all bunker rakes are left inside the hazard lines after play. Have a great Caddi's Day!",
                    datePosted = todayDate,
                    isPinned = true
                )
            )

            // Auto-generate today's daily chart!
            generateDailyChartIfNotExists(todayDate)
        }
    }
}
