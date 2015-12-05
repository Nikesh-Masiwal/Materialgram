package workingsaturdays.com.materialgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import workingsaturdays.com.materialgram.R;
import workingsaturdays.com.materialgram.Utils;
import workingsaturdays.com.materialgram.view.SquaredImageView;

/**
 * Created by Nikesh on 12/5/15.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;

    private int lastAnimatedPosition = -1;
    private int itemCount = 0;

    private OnFeedItemClickListner onFeedItemClickListner;


    private static final int ANIMATED_ITEMS_COUNT = 2;


    public FeedAdapter(Context ctx){

        this.context = ctx;

        updateItems();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_feed,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        runEnterAnimation(holder.itemView, position);
        FeedViewHolder feedViewHolder = (FeedViewHolder) holder;

        feedViewHolder.ivFeedBottom.setOnClickListener(this);
        feedViewHolder.ivFeedBottom.setTag(position);

        if (position % 2 == 0){

            feedViewHolder.ivFeedCenter.setImageResource(R.drawable.ic_feed_center1);
            feedViewHolder.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_1);

        }else {

            feedViewHolder.ivFeedCenter.setImageResource(R.drawable.ic_feed_center2);
            feedViewHolder.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_2);

        }

    }


    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListner onFeedItemClickListener) {
        this.onFeedItemClickListner = onFeedItemClickListener;
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ivFeedBottom){
            onFeedItemClickListner.onCommentsClick(v, (Integer) v.getTag());
        }

    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivFeedCenter)SquaredImageView ivFeedCenter;

        @Bind(R.id.ivFeedBottom)ImageView ivFeedBottom;

        @Bind(R.id.ivFeedPersonImage)ImageView ivPersonImage;
        @Bind(R.id.ivFeedPersonNameTextView)TextView ivFeedPersonName;

        public FeedViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public void updateItems(){
        itemCount = 10;
        notifyDataSetChanged();
    }

    public interface OnFeedItemClickListner{
        public void onCommentsClick(View v,int position);
    }
}



