package com.example.aliosama.sillynamemaker.Activities.Personality;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.aliosama.sillynamemaker.Activities.NavDrawerActivity;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityQuestionsModel;
import com.example.aliosama.sillynamemaker.Model.PersonalityQuestionsDatabase;
import com.example.aliosama.sillynamemaker.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PersonalityTestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener , View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static FloatingActionButton fab_next, fab_before;
    private  SectionsPagerAdapter mSectionsPagerAdapter;
    private  AVLoadingIndicatorView avi;
    static LinearLayout PageLayout;
    private ViewPager mViewPager;
    private NumberProgressBar numberProgressBar;
    private Button Submit;
    static boolean checked= false;
    List<PersonalityQuestionsModel.Fields.Question> PersonalityQuestions;
    private PersonalityQuestionsDatabase mPersonalityQuestionsDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_test);
        try {

            Init();

            fab_next.setOnClickListener(this);

            fab_before.setOnClickListener(this);

            mViewPager.addOnPageChangeListener(this);

            Submit.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init() {
        try {
            avi = findViewById(R.id.activity_personality_test_avi);
            PageLayout = findViewById(R.id.activity_personality_test_PageLayout);
            numberProgressBar = findViewById(R.id.number_progress_bar);
            numberProgressBar.setMax(100);
            fab_next = findViewById(R.id.fab_next);
            fab_before = findViewById(R.id.fab_before);
            Submit = findViewById(R.id.activity_personality_test_submit_Button);

            PersonalityQuestions = new ArrayList<>();
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mPersonalityQuestionsDatabase = new PersonalityQuestionsDatabase(PersonalityTestActivity.this);
            mPersonalityQuestionsDatabase.getAllQuestions();

            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.container);

            if (PersonalityQuestions != null && PersonalityQuestions.size() > 0) {
                Log.i("Init", "Question is Here");
                LoadQuestionsToAdapter(PersonalityQuestions);
            } else {
                startAnim();
                Log.i("Init", "Question is not Here yet");
            }
            PlaceholderFragment.setPersonalityTestReference(PersonalityTestActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void LoadQuestionsToAdapter(List<PersonalityQuestionsModel.Fields.Question> mPersonalityQuestions){
        try {
            PersonalityQuestions = mPersonalityQuestions;
            mSectionsPagerAdapter.SetQuestions(mPersonalityQuestions);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mSectionsPagerAdapter.notifyDataSetChanged();
            stopAnim();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            if (position == 0) {
                fab_before.setVisibility(View.INVISIBLE);
            } else if (position == 59) {
                fab_next.setVisibility(View.INVISIBLE);
                Submit.setVisibility(View.VISIBLE);

            } else {
                Submit.setVisibility(View.INVISIBLE);
                fab_before.setVisibility(View.VISIBLE);
                fab_next.setVisibility(View.VISIBLE);
            }

            if ((position + 1) % 6 == 0) {
                Log.i("onPageSelected", "position is divided by 6");
                numberProgressBar.setProgress(10 * ((position + 1) / 6));
            } else if ((position + 1) < 6) {
                numberProgressBar.setProgress(0);
            }
            Log.i("onPageSelected", "" + String.valueOf(position + 1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_next:
                try {
                    if (mSectionsPagerAdapter.getCount() != mViewPager.getCurrentItem())
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    Log.i("Fab_next",String.valueOf(mViewPager.getCurrentItem()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.fab_before:
                try {
                    if (mViewPager.getCurrentItem() != 0)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                    Log.i("Fab_before",String.valueOf(mViewPager.getCurrentItem()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.activity_personality_test_submit_Button:
                try {
                    Intent intent = new Intent(PersonalityTestActivity.this, PersonalityTestResultActivity.class);
                    intent.putExtra("QuestionsData", (ArrayList<PersonalityQuestionsModel.Fields.Question>) PersonalityQuestions);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    public void startAnim(){
        try {
            avi.smoothToShow();
            PageLayout.setVisibility(View.GONE);
            avi.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopAnim(){
        try {
            avi.hide();
            avi.setVisibility(View.GONE);
            PageLayout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
        TextView Question;
        RadioButton Agree,MidAgree,LowAgree,Natural,LowDisAgree,MidDisAgree,DisAgree;
        RadioGroup radioGroup;
        View rootView;
        private RadioButton Choices[] ;
        private int RadioButtonUnChecked[] = {
                R.drawable.test_personality_radio_button_agree,
                R.drawable.test_personality_radio_button_mid_agree,
                R.drawable.test_personality_radio_button_low_agree,
                R.drawable.test_personality_radio_button_natural,
                R.drawable.test_personality_radio_button_low_disagree,
                R.drawable.test_personality_radio_button_mid_disagree,
                R.drawable.test_personality_radio_button_disagree};

        private int RadioButtonChecked[] = {
                R.drawable.test_personality_radio_button_agree_checked,
                R.drawable.test_personality_radio_button_mid_agree_checked,
                R.drawable.test_personality_radio_button_low_agree_checked,
                R.drawable.test_personality_radio_button_natural_checked,
                R.drawable.test_personality_radio_button_low_disagree_checked,
                R.drawable.test_personality_radio_button_mid_disagree_checked,
                R.drawable.test_personality_radio_button_disagree_checked};


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static PersonalityTestActivity mPersonalityTestActivity;

        public PlaceholderFragment(){

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_personality_test, container, false);
            try {
                Init(rootView);
                InstructionsSequenceConfig();
                radioGroup.setOnCheckedChangeListener(this);
            }catch (Exception e){
                e.printStackTrace();
            }

            return rootView;
        }

        public static void setPersonalityTestReference(PersonalityTestActivity personalityTestActivity){
            mPersonalityTestActivity = personalityTestActivity;
        }

        private void InstructionsSequenceConfig() {
            try {
                if (getActivity() != null && !checked) {
                    checked = true; // this prevent fragment from loading twice.

                    ShowcaseConfig config = new ShowcaseConfig();
                    config.setDelay(100); // .1 second between each showcase view.
                    MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "ID2");
                    sequence.setConfig(config);
                    sequence.addSequenceItem(Question,
                            "This is Personality Question", "OK");
                    sequence.addSequenceItem(Agree,
                            "This is Most Agree Choice", "OK");
                    sequence.addSequenceItem(DisAgree,
                            "This is Most DisAgree Choice", "OK");
                    sequence.addSequenceItem(Natural,
                            "Natural Choice, Please try to avoid it", "OK");
                    sequence.addSequenceItem(fab_next,
                            "Next Question", "OK");
                    sequence.start();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void Init(View rootView) {

            Question = rootView.findViewById(R.id.fragment_personality_test_Question_TextView);
            Agree = rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonAgree);
            MidAgree = rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonMidAgree);
            LowAgree= rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonLowAgree);
            Natural= rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonNatural);
            LowDisAgree= rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonLowDisagree);
            MidDisAgree= rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonMidDisagree);
            DisAgree= rootView.findViewById(R.id.fragment_personality_test_Question_RadioButtonDisagree);
            radioGroup = rootView.findViewById(R.id.fragment_personality_test_Question_RadioGroup);
            Choices = new RadioButton[7];
            Choices[0] = Agree;
            Choices[1] = MidAgree;
            Choices[2] = LowAgree;
            Choices[3] = Natural;
            Choices[4] = LowDisAgree;
            Choices[5] = MidDisAgree;
            Choices[6] = DisAgree;

            try {
                if (getArguments() != null && getArguments().get("Question") != null) {
                    Question.setText(getArguments().getString("Question"));
                    Log.i("Fragment Init", getArguments().get("Question").toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            try {
                for (int index = 0; index < Choices.length; index++){
                    if (Choices[index].getId() == checkedId){
                        Choices[index].setChecked(true);
                        Choices[index].setButtonDrawable(RadioButtonChecked[index]);
                        setQuestionValue(checkedId);
                    }else {
                        Choices[index].setChecked(false);
                        Choices[index].setButtonDrawable(RadioButtonUnChecked[index]);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void setQuestionValue(int id) {
            try {
                for (int i = 0; i < Choices.length; i++) {
                    if (Choices[i].getId() == id) {
                        if (i < 3) {
                            mPersonalityTestActivity.PersonalityQuestions.get(getArguments().getInt("Position")).setQuestionValue(1);
                        } else if (i == 4) {
                            mPersonalityTestActivity.PersonalityQuestions.get(getArguments().getInt("Position")).setQuestionValue(0);
                        } else {
                            mPersonalityTestActivity.PersonalityQuestions.get(getArguments().getInt("Position")).setQuestionValue(-1);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        static List<PersonalityQuestionsModel.Fields.Question> Questions;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Questions = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment fragment ;
            fragment = PlaceholderFragment.newInstance(position + 1);
            try {
                if (fragment.getArguments() != null) {
                    Bundle args = fragment.getArguments();
                    if (Questions != null && Questions.size() > 0) {
                        if (args != null) {
                            args.putString("Question", Questions.get(position).getQuestion());
                        }
                    }
                    fragment.setArguments(args);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return fragment;
        }

        public void SetQuestions(List<PersonalityQuestionsModel.Fields.Question> questions){
            Questions = questions;
        }
        @Override
        public int getCount() {
            // Show 60 total pages.
            return 60;
        }
    }
}
