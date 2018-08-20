package com.example.labtwoausecondversion.ui.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.labtwoausecondversion.BuildConfig;
import com.example.labtwoausecondversion.R;
import com.example.labtwoausecondversion.config.AppConstants;
import com.example.labtwoausecondversion.ui.favorites.FavoritesActivity;
import com.example.labtwoausecondversion.ui.search.SearchActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class CustomDrawer extends AppCompatActivity {
    private Activity mActivity;

    public void initDrawer(Activity activity, Toolbar toolbar) {
        if (activity == null || toolbar == null) {
            return;
        }
        mActivity = activity;
        new DrawerBuilder()
                .withActivity(mActivity)
                .withToolbar(toolbar)
                .addDrawerItems(getDrawerItems())
                .withAccountHeader(getAccountHeader())
                .withOnDrawerItemClickListener(mOnDrawerItemClickListener)
                .build();
    }

    private IDrawerItem[] getDrawerItems() {
        String[] itemTitles = mActivity.getResources().getStringArray(R.array.drawer_item_titles);
        int[] resImages = new int[itemTitles.length];
        resImages[0] = R.drawable.ic_search_dark_grey_24dp;
        resImages[1] = R.drawable.ic_star_dark_grey_24dp;
        resImages[2] = R.drawable.ic_exit_to_app_dark_grey_24dp;
        IDrawerItem[] iDrawerItems = new IDrawerItem[itemTitles.length];
        for (int i = 0; i < itemTitles.length; i++) {
            iDrawerItems[i] = new PrimaryDrawerItem()
                    .withName(itemTitles[i])
                    .withIcon(resImages[i])
                    .withIdentifier(i+10);
        }
        return iDrawerItems;
    }

    private AccountHeader getAccountHeader() {
        return new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.drawable.logo)
                        .withName(R.string.all_vacancies_in_kg)
                        .withEmail(BuildConfig.VERSION_APP))
                .withSelectionListEnabledForSingleProfile(false)
                .build();
    }

    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            switch (position) {
                case 1:
                    Intent searchIntent = new Intent(mActivity.getApplicationContext(), SearchActivity.class);
                    startActivityForResult(searchIntent, AppConstants.REQUEST_CODE);
                    break;
                case 2:
                    Intent favoritesIntent = new Intent(mActivity.getApplicationContext(), FavoritesActivity.class);
                    startActivityForResult(favoritesIntent, AppConstants.REQUEST_CODE);
                    break;
                case 3:
                    if (mActivity != null) {
                        mActivity.finish();
                    }
                    break;
            }
            return false;
        }
    };
}
