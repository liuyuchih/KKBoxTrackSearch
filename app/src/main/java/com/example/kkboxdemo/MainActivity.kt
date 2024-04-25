package com.example.kkboxdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kkboxdemo.databinding.ActivityMainBinding
import com.example.kkboxdemo.databinding.TrackItemBinding
import com.example.kkboxdemo.remote.Constants.Companion.LOGCAT_TAG
import com.example.myapplication.remote.Track

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = (application as MyApplication).repository
        val factory = MainViewModel.Factory(repository)
        val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        binding.list.layoutManager = LinearLayoutManager(this)
        val adapter = TracksAdapter()
        binding.list.adapter = adapter

        viewModel.searchResults.observe(this) {
            Log.d(LOGCAT_TAG, "observer: " + it.size)
            val newList = ArrayList(it)
            adapter.submitList(newList)
        }

        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                val threshold = 5
                if (totalItemCount <= lastVisibleItem + threshold) {
                    Log.d(LOGCAT_TAG, "load more...")
                    viewModel.loadMoreData()
                }
            }
        })

        binding.search.setOnClickListener {
            val query = binding.input.text.toString()
            viewModel.searchTracks(query)
            hideSoftKeyboard()
        }

        binding.scrollToToop.setOnClickListener() {
            binding.list.scrollToPosition(0)
        }

        binding.clearAll.setOnClickListener {
            viewModel.clearResult()
        }

        viewModel.isLoading.observe(this) {
            binding.loading.visibility = if(it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = this.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    class TrackDiffUtil: DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }

    class TrackViewHolder(private val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Track, position: Int) {
            binding.song.text = "$position.\n${item.name}"
            binding.singer.text = item.album.artist.name
            Glide
                .with(binding.root.context)
                .load(item.album.images[1].url)
                .centerCrop()
                .into(binding.album)
        }
    }

    class TracksAdapter : ListAdapter<Track, TrackViewHolder>(TrackDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
            val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TrackViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            val item = getItem(position)
            holder.onBind(item, position)
        }

    }
}