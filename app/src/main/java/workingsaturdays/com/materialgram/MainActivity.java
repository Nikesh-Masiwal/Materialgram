package workingsaturdays.com.materialgram;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import workingsaturdays.com.materialgram.activity.CommentsActivity;
import workingsaturdays.com.materialgram.adapters.FeedAdapter;

public class MainActivity extends AppCompatActivity implements FeedAdapter.OnFeedItemClickListner {

    @Bind(R.id.rvFeed)RecyclerView rvFeed;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.ivLogo)ImageView ivLogo;
    @Bind(R.id.btnCreate)ImageButton btnCreate;

    private FeedAdapter feedAdapter;
    private MenuItem inboxMenuItem;

    private boolean pendingIntroAnimation = true;

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        setupToolbar();
        setupFeed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);

        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }

        return true;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
    }



    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){

            /**
             * <p>Returns the amount of extra space that should be laid out by LayoutManager.
             * By default, {@link LinearLayoutManager} lays out 1 extra page of
             * items while smooth scrolling and 0 otherwise. You can override this method to implement your
             * custom layout pre-cache logic.</p>
             * <p>Laying out invisible elements will eventually come with performance cost. On the other
             * hand, in places like smooth scrolling to an unknown location, this extra content helps
             * LayoutManager to calculate a much smoother scrolling; which improves user experience.</p>
             * <p>You can also use this if you are trying to pre-layout your upcoming views.</p>
             *
             * @param state
             * @return The extra space that should be laid out (in pixels).
             */
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 700;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(this);
        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
    }

    private void startIntroAnimation() {
        btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        ivLogo.setTranslationY(-actionbarSize);
        inboxMenuItem.getActionView().setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        ivLogo.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        inboxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        btnCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        feedAdapter.updateItems();
    }


    @Override
    public void onCommentsClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);


        //Get location on screen
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION,startingLocation[1]);
        startActivity(intent);
        //Disable enter transition for new Acitvity
        overridePendingTransition(0, 0);
    }
}
