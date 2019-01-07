package net.vokacom.drive.mListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.vokacom.drive.R;
import net.vokacom.drive.mData.Product;
import net.vokacom.drive.mData.ProductMen;

import java.util.List;


/**
 * Created by emma on 10/18/2017.
 */

public class ProductAdapterMen extends RecyclerView.Adapter<ProductAdapterMen.ProductViewHolder> {

    private Context mCtx;
    private List<ProductMen> productListMen;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductAdapterMen(Context mCtx, List<ProductMen> productListMen) {
        this.mCtx =  mCtx;
        this.productListMen = productListMen;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_layout_content_men, parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductMen product = productListMen.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);

        holder.textViewID.setText(String.valueOf(product.getId()));
        holder.textViewTitle.setText(String.valueOf(product.getTitle()));
        holder.textViewShortDesc.setText(String.valueOf(product.getshort_description()));
        holder.textViewRating.setText(String.valueOf(product.getRating()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));

    }

    @Override
    public int getItemCount() {
        return productListMen.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewID,textViewPrice;
        ImageView imageView;

        ProductViewHolder(View itemView) {
            super(itemView);

            textViewID = itemView.findViewById(R.id.textViewId);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productListMen != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.OnItemClick(position);

                        }
                    }

                }
            });
        }
    }
}