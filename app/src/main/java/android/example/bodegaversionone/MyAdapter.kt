package android.example.bodegaversionone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_items.view.*



class MyAdapter(val context: Context, val userList: List<MyDataItem>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // image resource is String type for url link

        var title: TextView
        var description: TextView
        var image: ImageView

        init {
            title = itemView.title
            description = itemView.description
            image = itemView.image_movie
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //updated all code from databinding to synthetic binding

        holder.title.text = userList[position].title.toString()
        holder.description.text = userList[position].description.toString()

        val url = userList[position].image

        Glide.with(holder.image) // Now using Glide with holder directly
            .load(url)
            .into(holder.image)



    }

    override fun getItemCount(): Int {
        return userList.size
    }
}