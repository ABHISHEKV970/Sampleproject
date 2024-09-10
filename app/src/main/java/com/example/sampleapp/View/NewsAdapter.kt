package com.example.sampleapp.View

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.Models.Article
import com.example.sampleapp.R
import com.squareup.picasso.Picasso


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList = mutableListOf<Article>()

    fun setData(data: List<Article>) {
        newsList.clear()
        newsList.addAll(data)
        notifyDataSetChanged()
    }

    fun sortData(data: List<Article>)
    {
        newsList.clear()
        newsList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = newsList[position]
        holder.bind(article)

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", newsList[position].url)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        Log.i("sksksks", newsList.size.toString())
        return newsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val authorTextView: TextView = itemView.findViewById(R.id.author_name)
        private val titleTextView: TextView = itemView.findViewById(R.id.title_name)
        private val publishedDateTextView: TextView = itemView.findViewById(R.id.publishedDate)
        private val coverimage : ImageView = itemView.findViewById(R.id.cover_image)

        fun bind(news: Article) {
            try {
                authorTextView.text = news.author
                titleTextView.text = news.title
                publishedDateTextView.text = "Published Date : " + news.publishedAt
                Picasso.get().load(news.urlToImage).into(coverimage)
            }
            catch (e : Exception)
            {
                Log.i("error",e.toString())
            }

        }
    }
}