package com.example.sampleapp.View

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.Models.Article
import com.example.sampleapp.Network.FetchData
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityMainBinding
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.model.AppStatus
import com.moengage.core.model.GeoLocation
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.ui.MoEInboxUiHelper
import com.moengage.inbox.ui.view.InboxActivity
import com.moengage.inbox.ui.view.InboxFragment
import com.moengage.pushbase.MoEPushHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private lateinit var sortitems: MutableList<Article>
    private var isSorted = false
    private var checkinstalled = true

    val currentDate = Date()
    val format = SimpleDateFormat("yyyy-MM-dd")
    var formattedDate = ""

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        formattedDate = format.format(currentDate)


        val bundle = Bundle()

        bundle?.let {

                var value = bundle.getBundle("company")

                value?.let {

                    if (value.equals("moengage")) {
                        //For handling custom notification click
                        MoEPushHelper.getInstance().logNotificationClick(applicationContext, intent)
                    }

                }
                }

        binding.newsRv.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter()
        binding.newsRv.adapter = adapter

        try {
            if (checkForInternet(this)) {
                if (checkinstalled) {
                    MoEAnalyticsHelper.setAppStatus(this, AppStatus.INSTALL)
                } else {
                    MoEAnalyticsHelper.setAppStatus(this, AppStatus.UPDATE)

                }

                api_call()

                binding.sort.setOnClickListener {

                    sort_track()

                    sortIt()

                }

            } else {

                Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.i("error", e.toString())
        }

        val  unclickedCount = MoEInboxHelper.getInstance().getUnClickedMessagesCount(applicationContext)
        unclickedCount?.let {
            Log.i("counthere",unclickedCount.count.toString())
            binding.countBadge.text = unclickedCount.count.toString()
        }


        binding.event1.setOnClickListener {

            event_track()
        }

        binding.event2.setOnClickListener {
            event2_track()
        }



        binding.bellicon.setOnClickListener{

            //Custom Adapter Call
//            MoEInboxUiHelper.getInstance().setInboxAdapter(CustomInboxAdapter())

            //Activity call
//            val intent = Intent(this, InboxActivity::class.java)
//            startActivity(intent)

            //Fragment call
//            val intent = Intent(this, FragmentParentActivity::class.java)
//            startActivity(intent)

            //created activity with recyclerview
            val intent = Intent(this, CustomInboxActivity::class.java)
            startActivity(intent)

        }




    }



    private fun event2_track() {
        val event2_track = Properties()
        event2_track
            .addAttribute("quantity", 2)
            .addAttribute("Content", "Triggered Event 2")
            .addAttribute("Date", formattedDate)
            .addAttribute("Location", GeoLocation(20.7, 20.22))
            .addAttribute("jsonArrayAttr", JSONArray(listOf(4,5, 6)))
        MoEAnalyticsHelper.trackEvent(this, "Triggering Event 2", event2_track)
    }

    private fun event_track() {
        val event1_track = Properties()
        event1_track
            .addAttribute("quantity", 2)
            .addAttribute("Content", "Triggered Event 1")
            .addAttribute("Date", formattedDate)
            .addAttribute("Location", GeoLocation(20.7, 20.22))
            .addAttribute("jsonArrayAttr", JSONArray(listOf(4,5, 6)))
        MoEAnalyticsHelper.trackEvent(this, "Triggering Event 1", event1_track)
    }

    private fun sort_track() {
        val sort_tracks = Properties()
        sort_tracks
            .addAttribute("quantity", 2)
            .addAttribute("Content", "Sorting Event")
            .addAttribute("Date", formattedDate)
            .addAttribute("Location", GeoLocation(40.77, 19.22))
            .addAttribute("jsonArrayAttr", JSONArray(listOf(1, 2, 3)))
        MoEAnalyticsHelper.trackEvent(this, "Sorting Event", sort_tracks)

    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun api_call() {

        val fetchData = FetchData { newsList ->
            withContext(Dispatchers.Main) {
                adapter.setData(newsList)
                sortitems = mutableListOf()
                for (article in newsList) {
                    val newArticle = Article(
                        article.author,
                        article.content,
                        article.description,
                        article.publishedAt,
                        article.source,
                        article.title,
                        article.url,
                        article.urlToImage,
                    )
                    sortitems.add(newArticle)
                }
            }
        }

        GlobalScope.launch {
            fetchData.fetchData()
        }

    }


    private fun sortIt() {

        isSorted = !isSorted
        val sortedItems = if (isSorted) {
            sortitems.sortedBy { it.publishedAt }
        } else {
            sortitems
        }
        adapter.sortData(sortedItems)
    }


    override fun onRestart() {
        super.onRestart()
        if (checkForInternet(this)) {
            api_call()
            val  unclickedCount = MoEInboxHelper.getInstance().getUnClickedMessagesCount(applicationContext)
            unclickedCount?.let {
                Log.i("counthere",unclickedCount.count.toString())
                binding.countBadge.text = unclickedCount.count.toString()
            }
        } else {
            Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_SHORT).show()
        }


    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.notificatio_item -> {
//
//                //Activity call
//                val intent = Intent(this, InboxActivity::class.java)
//                startActivity(intent)
//
//                //Fragment call
////                supportFragmentManager.commit {
////                    setReorderingAllowed(true)
////                    add<InboxFragment>(R.id.fragment_container_view)
////                }
//
//                true
//            }
//            R.id.logout_item -> {
//
//                MoECoreHelper.logoutUser(this)
//
//                finish()
//
//                true
//            }
//            R.id.settings_item -> {
//
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}