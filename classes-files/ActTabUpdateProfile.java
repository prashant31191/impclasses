package com.testapp;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.api.model.MemberDetailDataModel;
import ca.api.model.MemberExpModel;
import ca.api.response.MemberDetailResponse;
import com.testapp.fragment.AddClinicInfoFragment;
import com.testapp.fragment.ContactInfoFragment;
import com.testapp.fragment.EducationInfoFragment;
import com.testapp.fragment.GeneralInfoFragment;
import com.testapp.fragment.PracticeInfoFragment;
import com.testapp.fragment.SocialProfileFragment;
import ca.utils.AppFlags;
import ca.utils.StaticDataList;
import ca.utils.TintableImageView;
import retrofit2.Call;


public class ActTabUpdateProfile extends BaseActivity {

    String TAG = "=ActTabUpdateProfile=";
    String strFrom = "", strData = "", strMemberId = "";


    @BindView(R.id.htab_appbar)
    AppBarLayout htab_appbar;

    @BindView(R.id.htab_viewpager)
    ViewPager viewPager;

    @BindView(R.id.htab_tabs)
    TabLayout tabLayout;

    @BindView(R.id.htab_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;


    ViewPagerAdapter viewPagerAdapter;


    boolean blnCallResumeApi = false;
    Call callRetrofit;
    MemberDetailResponse memberDetailResponse;
    public static MemberDetailDataModel memberDetailDataModel;


    private ArrayList<MemberExpModel> arrayListAllMemberExpModel = new ArrayList<>();


    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            App.showLog(TAG);
            ViewGroup.inflate(this, R.layout.act_tab_update_profile, llContainerSub);
            ButterKnife.bind(this);

            getIntentData();
            initialization();
            setClickEvents();
            setStaticData();


            App.getInstance().trackScreenView(getString(R.string.scrn_ActTabUpdateProfile));
        } catch (Exception e) {
            // TODO: handle exceptione.
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

          /*  if (blnCallResumeApi == true) {
                asyncGetMemberData();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIntentData() {
        Bundle bundle;
        if (getIntent() != null && getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getString(AppFlags.tagFrom) != null) {
                strFrom = bundle.getString(AppFlags.tagFrom);
            }

            if (bundle.getString(AppFlags.tagData) != null) {
                strData = bundle.getString(AppFlags.tagData);
            }
        }

        App.showLog("====strFrom===" + strFrom);
        App.showLog("===strData====" + strData);

    }

    private void initialization() {
        try {


            rlBaseMainHeader.setVisibility(View.VISIBLE);
            rlMenu.setVisibility(View.GONE);
            rlBack.setVisibility(View.VISIBLE);

            setEnableDrawer(false);
            showBottomBar(false);
            tvTitle.setText("General Information");
            setTabSelected(0);

            collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.clrTheme));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setClickEvents() {
        try {
            rlBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*

    // PatientExperience API Call
    public void asyncGetMemberData(String strMemberId) {

        try {
            blnCallResumeApi = true;
            customProgressDialog.show();

            //RequestBody r_email = App.createPartFromString(strEmail);
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("member_id", App.createPartFromString(strMemberId));
            hashMap.putAll(App.addCommonHashmap());


            callRetrofit = App.getRetrofitApiService().getMemberDetail(App.OP_MEMBER, hashMap);


            callRetrofit.enqueue(new Callback<MemberDetailResponse>() {
                @Override
                public void onResponse(Call<MemberDetailResponse> call, Response<MemberDetailResponse> response) {
                    try {
                        customProgressDialog.dismiss();
                        memberDetailResponse = response.body();

                        if (memberDetailResponse == null) {

                            App.showLog("Test---null response--", "==Something wrong=");
                            ResponseBody responseBody = response.errorBody();
                            if (responseBody != null) {
                                try {
                                    App.showLog("Login---error-", " -/- " + responseBody.string());

                                    if (response.code() == AppFlags.INT_INVALID_TOKEN) {
                                        asyncLogout();
                                    } else {
                                        App.showSnackBar(tvTitle, getString(R.string.strSomethingWentwrong));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            //200 sucess
                            App.showLogApiRespose(TAG + App.OP_MEMBER, response);

                            if (memberDetailResponse != null && memberDetailResponse.status != null) {
                                if (memberDetailResponse.status.equalsIgnoreCase("1")) {
                                    if (memberDetailResponse.memberDetailDataModel != null) {
                                        memberDetailDataModel = memberDetailResponse.memberDetailDataModel;
                                        setStaticData();
                                    } else {
                                        App.showLog(TAG + "======login else========");
                                        App.showSnackBar(tvTitle, getString(R.string.strSomethingWentwrong));
                                    }

                                } else if (memberDetailResponse.status.equalsIgnoreCase(AppFlags.INVALID_TOKEN)) {
                                    asyncLogout();
                                } else {
                                    App.showLog(TAG + "======login else========");
                                    if (memberDetailResponse.msg != null) {
                                        App.showLog(TAG + memberDetailResponse.msg);
                                        App.showSnackBar(tvTitle, memberDetailResponse.msg);
                                    } else {
                                        App.showLog(TAG + "======login else========");
                                        App.showSnackBar(tvTitle, getString(R.string.strSomethingWentwrong));
                                    }
                                }
                            } else {
                                App.showLog(TAG + "======login else========");
                                App.showSnackBar(tvTitle, getString(R.string.strSomethingWentwrong));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MemberDetailResponse> call, Throwable t) {
                    t.printStackTrace();
                    customProgressDialog.dismiss();
                    App.showSnackBar(tvTitle, getString(R.string.strSomethingWentwrong));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            customProgressDialog.dismiss();
        }
    }

*/

    private void setStaticData() {
        try {

            App.showLog("=======setStaticData===");
            //setupViewPager(viewPager);
            //111   tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_txt_color));

            arrayListAllMemberExpModel = StaticDataList.getProfileUpdateLit();



            createViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            //createTabIcons();
            setUpViewPagerIcons(tabLayout);




        /*    //Add fragments
            PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new GeneralInfoFragment());
            adapter.addFragment(new GeneralInfoFragment());
            adapter.addFragment(new GeneralInfoFragment());

            //Setting adapter
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GeneralInfoFragment(), "General Information");
        adapter.addFrag(new ContactInfoFragment(), "Contact Information");
        adapter.addFrag(new SocialProfileFragment(), "Social Profile");
        adapter.addFrag(new EducationInfoFragment(), "Education Information");
        adapter.addFrag(new PracticeInfoFragment(), "Practice Information");
        adapter.addFrag(new AddClinicInfoFragment(), "Clinic Information");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }














    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Tab 1");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, arrayListAllMemberExpModel.get(1).icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Tab 2");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0,arrayListAllMemberExpModel.get(2).icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Tab 3");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0,arrayListAllMemberExpModel.get(3).icon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }
    private void setUpViewPagerIcons(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
           // TabLayout.Tab tabitem = tabLayout.newTab();
            TintableImageView tab1 = (TintableImageView) LayoutInflater.from(this).inflate(R.layout.tint_layout, null);
            tab1.setImageResource(arrayListAllMemberExpModel.get(i).icon);
          /*  tabitem.setCustomView(tab1);
            tabLayout.addTab(tabitem);*/
            tabLayout.getTabAt(i).setCustomView(tab1);
        }
    }


    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SpannableString sb = new SpannableString(" ");// " title name"
            try {

                Drawable image = ContextCompat.getDrawable(ActTabUpdateProfile.this, arrayListAllMemberExpModel.get(position).icon);
                //Drawable image = ContextCompat.getDrawable(ActTabUpdateProfile.this, R.drawable.ic_tw);

                image.setBounds(0, 0, (int) App.convertDpToPixel(28, ActTabUpdateProfile.this), (int) App.convertDpToPixel(28, ActTabUpdateProfile.this));

                ImageSpan imageSpan = new ImageSpan(image);
                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sb;
            } catch (Exception e) {
                e.printStackTrace();
                return sb;
            }
        }
    }



}