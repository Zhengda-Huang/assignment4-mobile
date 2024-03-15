import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment4.R
import com.cs4520.assignment4.databinding.FragmentProductItemBinding
import com.cs4520.assignment4.model.database.Product
import com.cs4520.assignment4.productsDataset

class ProductListAdapter(private var productList: List<Product> = emptyList()) :
    RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {

    inner class ProductListViewHolder(private val binding: FragmentProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            val drawableId =
                if (product.type == "Equipment") R.drawable.tools else R.drawable.vegetable

            val color =
                if (product.type == "Equipment") R.color.red else R.color.light_yellow
            val backgroundColor = ContextCompat.getColor(binding.root.context, color)

            binding.root.setBackgroundColor(backgroundColor)
            binding.itemImageView.setImageResource(drawableId)
            binding.itemNameTextView.text = product.name
            if (product.expiryDate == null){
                binding.itemExpiryTextView.visibility = GONE
            }else{
                binding.itemExpiryTextView.text = product.expiryDate
            }
            binding.itemPriceTextView.text = product.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val binding =
            FragmentProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    fun submitList(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }
}
