package me.unmsm.alejandriapp.presentacion.presenter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.ArrayList;

import me.unmsm.alejandriapp.data.entity.loginData;
import me.unmsm.alejandriapp.data.repositories.remote.Request.UserRequest;
import me.unmsm.alejandriapp.data.repositories.remote.ServiceGenerator;
import me.unmsm.alejandriapp.presentacion.contract.LoginContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;
    private Context context;
    private ArrayList<loginData> loginOpen;


    public LoginPresenter(LoginContract.View mLoginView, @NonNull Context context) {
        this.mLoginView = mLoginView;
        this.context = context;
        this.mLoginView.setPresenter(this);
    }

    @Override
    public void loginUser(final String username, final String password) {
        mLoginView.setLoadingIndicator(true);
        final UserRequest loginService = ServiceGenerator.createService(UserRequest.class);
        final Long contrasenha = Long.parseLong(password);
        Call<ArrayList<loginData>> call = loginService.getLogin(username, password);
        call.enqueue(new Callback<ArrayList<loginData>>() {
            @Override
            public void onResponse(Call<ArrayList<loginData>> call, Response<ArrayList<loginData>> response) {
                if (response.isSuccessful()) {
                    loginOpen = response.body();
                    Log.i("Estado", "entro por aca");
                    Log.i(username, String.valueOf(password));
                    for (int i = 0; i < loginOpen.size(); i++) {
                        if (username.equals(loginOpen.get(i).getEmail()) && contrasenha.equals(loginOpen.get(i).codUsuario)) {
                            Log.i(loginOpen.get(i).getEmail(), String.valueOf(loginOpen.get(i).getCodUsuario()));
                            mLoginView.successLoginUser();
                            mLoginView.setDialogMessage("Bienvenido");

                        }
                    }

                }
                mLoginView.setLoadingIndicator(false);
                mLoginView.errorLogin("Email o contraseña incorrecto(s)");
            }

            @Override
            public void onFailure(Call<ArrayList<loginData>> call, Throwable t) {
                mLoginView.setLoadingIndicator(false);
                mLoginView.errorLogin("Ocurrió un error al tratar de ingresar, por favor intente de nuevo");
            }
        });
    }

    @Override
    public void getPerfilUser() {

    }

    @Override
    public void openSessionUser(String token, loginData userEntity) {

    }

    @Override
    public void start() {

    }
}
