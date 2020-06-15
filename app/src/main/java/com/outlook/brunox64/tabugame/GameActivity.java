package com.outlook.brunox64.tabugame;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by bruno on 30/08/2016.
 */
public class GameActivity extends Activity {

    private View mDecorView;
    private HomeFragment mHomeFragment;
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DebugUtils.info(this, "onCreate");

        GameContext gameContext = new GameContext(getResources().getDisplayMetrics().density);
        gameContext.setOperAndTab(Operacao.MULTIPLICACAO, Tabuada.DOIS);

        setContentView(R.layout.activity_game);

        mDecorView = getWindow().getDecorView();
        setFullScreen();

        mHomeFragment = new HomeFragment();
        mHomeFragment.setOnViewCreatedListener(new HomeFragment.ViewCreatedListener() {
            @Override
            public void onViewCreated() {
                registerHomeListener();
            }
        });

        mGameFragment = new GameFragment();
        mGameFragment.setOnViewCreatedListener(new GameFragment.ViewCreatedListener() {
            @Override
            public void onViewCreated() {
                registerTableListener();
            }
        });

        gameContext.recuperarResultados(getPreferences(Context.MODE_PRIVATE));

        showHome();
    }

    private void registerTableListener() {
        GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.setOnCloseListener(new GameView.CloseListener() {
            @Override
            public void onClose() {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });
    }

    private void registerHomeListener() {
        HomeView homeView = (HomeView) findViewById(R.id.homeView);
        homeView.setOnClickBtnListener(new HomeView.ClickBtnListener() {
            @Override
            public void onClick(ButtonTab btn) {
                GameContext.get().setOperAndTab(btn.getOper(), btn.getTab());
                showTable();
            }
        });
    }

    private void setFullScreen() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | mDecorView.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showTable() {
        DebugUtils.info(this, "showTable");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.gameActivity, mGameFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

        DebugUtils.info(this, "commit table");
    }

    private void showHome() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.gameActivity, mHomeFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        DebugUtils.info(this, "commit home");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            setFullScreen();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        GameContext.get().persistirResutados(getPreferences(Context.MODE_PRIVATE));
    }
}
