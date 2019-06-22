package com.example.moviecatalog.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.domain.Movie
import com.example.moviecatalog.utils.OnClickListener
import kotlinx.android.synthetic.main.card_view.view.*

class MoviesAdapter(private var movies: ArrayList<Movie>, private val clickListener: OnClickListener) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    /*init of ViewHolder*/
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_view, viewGroup, false
        )
    )

    override fun getItemCount(): Int = movies.size

    /*full data for each element of RV*/
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(position)

    fun setItems(movies: ArrayList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    fun addItems() {
        notifyItemInserted(movies.size - 1)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        init {
            /*set clickListener on elements RV*/
            v.setOnClickListener {
                clickListener.onCardViewClick(adapterPosition)
            }
        }

        fun bind(position: Int) {
            val movie = movies[position]

            setTitle(movie)
            setPoster(movie)
            setDescription(movie)
            setDate(movie)
            setLike(movie)
        }

        private fun setTitle(movie: Movie) {
            itemView.tv_title.text = movie.getTitle
        }

        private fun setPoster(movie: Movie) = Glide.with(itemView.cv_movie).load(movie.getPosterURL).into(itemView.image_poster)

        private fun setDescription(movie: Movie) {
            itemView.tv_description.text = movie.getDescription
        }

        private fun setDate(movie: Movie) {
            itemView.tv_date.text = movie.getDate
        }

        private fun setLike(movie: Movie) {
            itemView.image_heart.setOnClickListener {
                itemView.image_heart.setImageResource(R.drawable.ic_heart_fill)
            }
        }
    }
}