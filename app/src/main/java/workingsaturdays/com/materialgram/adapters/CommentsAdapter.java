package workingsaturdays.com.materialgram.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import workingsaturdays.com.materialgram.R;
import workingsaturdays.com.materialgram.utils.RoundedTransformation;

/**
 * Created by Nikesh on 12/6/15.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int itemsCount = 10;
    private int lastAnimatedPosition = -1;
    private int avatarSize;


    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private boolean newcommentadded = false;

    private String newcomment;


    public CommentsAdapter(Context context){
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.btn_fab_size);
        updateItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new CommentViewHolder(view);
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ivUserAvatar)ImageView ivUserAvatar;
        @Bind(R.id.tvComment)TextView tvComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        runEnterAnimation(holder.itemView, position);

        CommentViewHolder viewHolder = (CommentViewHolder) holder;

        if (newcommentadded){
            viewHolder.tvComment.setText(newcomment);

        }else {

            switch (position % 3) {
                case 0:
                    viewHolder.tvComment.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
                    break;
                case 1:
                    viewHolder.tvComment.setText("Cupcake ipsum dolor sit amet bear claw.");
                    break;
                case 2:
                    viewHolder.tvComment.setText("Cupcake ipsum dolor sit. Amet gingerbread cupcake. Gummies ice cream dessert icing marzipan apple pie dessert sugar plum.");
                    break;
            }
        }

        Picasso.with(context)
                .load(R.drawable.ic_launcher)
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(viewHolder.ivUserAvatar);

    }


    public void updateItems() {
        itemsCount = 10;
        notifyDataSetChanged();
    }

    public void addItem() {
//        newcomment = addThisComment;
//        newcommentadded = true;
        itemsCount++;
        notifyItemInserted(itemsCount - 1);
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }


    @Override
    public int getItemCount() {
        return itemsCount;
    }
}
