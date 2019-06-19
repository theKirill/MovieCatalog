package com.example.moviecatalog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviecatalog.R
import com.example.moviecatalog.models.Movie
import kotlinx.android.synthetic.main.card_view.view.*

class MoviesAdapter(private var movies: ArrayList<Movie>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_view, viewGroup, false
        )
    )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(position)

    fun setItems(movies: ArrayList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(position: Int) {
            val movie = movies[position]

            setTitle(movie)
            setDescription(movie)
            setDate(movie)
        }

        private fun setTitle(movie: Movie) {
            itemView.tv_title.text = movie.getTitle
        }

        private fun setDescription(movie: Movie) {
            itemView.tv_description.text = movie.getDescription
        }

        private fun setDate(movie: Movie) {
            itemView.tv_date.text = movie.getDate
        }
    }
}