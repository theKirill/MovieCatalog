package com.yanyushkin.moviecatalog.adapter

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.yanyushkin.moviecatalog.*
import com.yanyushkin.moviecatalog.domain.Movie
import com.yanyushkin.moviecatalog.utils.OnClickListener
import kotlinx.android.synthetic.main.card_view.view.*

class MoviesAdapter(private var movies: MutableList<Movie>, private val clickListener: OnClickListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    /**
     * init of ViewHolder
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(
            R.layout.card_view, viewGroup, false
        )
    )

    override fun getItemCount(): Int = movies.size

    /**
     * full data for each element of RV
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int): Unit = viewHolder.bind(position)

    fun setItems(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
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

        private fun setPoster(movie: Movie) =
            Glide.with(itemView.cv_movie).load(movie.getPosterURL).into(itemView.image_poster)

        private fun setDescription(movie: Movie) {
            itemView.tv_description.text = movie.getDescription
        }

        private fun setDate(movie: Movie) {
            val date = movie.getDate

            itemView.apply {
                if (date.isNotEmpty()) {
                    tv_date.text = date
                } else {
                    image_calendar.visibility = View.GONE
                    tv_date.visibility = View.GONE
                }
            }
        }

        private fun setLike(movie: Movie) {
            itemView.apply {
                /*check for ID in shared pref and set like*/
                var imageHeart =
                    if (context.getPreferences().hasLike(movie.getId))
                        R.drawable.ic_heart_fill
                    else
                        R.drawable.ic_heart

                movie.like = imageHeart == R.drawable.ic_heart_fill

                image_heart.setImageResource(imageHeart)

                image_heart.setOnClickListener {
                    if (movie.like) {
                        imageHeart = R.drawable.ic_heart
                        context.getPreferences().removeId(movie.getId)
                    } else {
                        imageHeart = R.drawable.ic_heart_fill
                        context.getPreferences().saveId(movie.getId)
                    }

                    movie.like = imageHeart == R.drawable.ic_heart_fill

                    image_heart.setImageResource(imageHeart)
                }
            }
        }
    }
}