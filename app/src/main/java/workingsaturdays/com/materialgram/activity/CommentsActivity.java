package workingsaturdays.com.materialgram.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import workingsaturdays.com.materialgram.R;
import workingsaturdays.com.materialgram.Utils;
import workingsaturdays.com.materialgram.adapters.CommentsAdapter;

public class CommentsActivity extends AppCompatActivity {

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.contentRoot)LinearLayout contentRoot;
    @Bind(R.id.rvComments)RecyclerView rvComments;
    @Bind(R.id.llAddComment)LinearLayout llAddComment;
    @Bind(R.id.commentsAddEditTxt)EditText addCommentEditTxt;

    private int drawingStartLocation;

    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ButterKnife.bind(this);

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION,0);
        commentsAdapter = new CommentsAdapter(getBaseContext());

        if (savedInstanceState == null){
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }

        setupToolbar();

        setupComments();


    }

    private void setupComments() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(this);
        rvComments.setAdapter(commentsAdapter);


        rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    @OnClick(R.id.btnSendComment)
    public void onSendCommentClick() {

        String addThisComment = addCommentEditTxt.getText().toString();

        commentsAdapter.addItem();
        commentsAdapter.setAnimationsLocked(false);
        commentsAdapter.setDelayEnterAnimation(false);
        rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
    }

    @Override
    public void onBackPressed() {
        contentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    private void startIntroAnimation() {
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(100);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

}
