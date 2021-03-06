package com.google.codelabs.mdc.java.shrine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.codelabs.mdc.java.shrine.R;
import com.google.codelabs.mdc.java.shrine.activities.MainActivity;
import com.google.codelabs.mdc.java.shrine.api.ApiService;
import com.google.codelabs.mdc.java.shrine.entities.City;
import com.google.codelabs.mdc.java.shrine.entities.District;
import com.google.codelabs.mdc.java.shrine.entities.MyResponse;
import com.google.codelabs.mdc.java.shrine.entities.RegisterForm;
import com.google.codelabs.mdc.java.shrine.entities.Ward;
import com.google.codelabs.mdc.java.shrine.entities.adapter.AdapterCity;
import com.google.codelabs.mdc.java.shrine.entities.adapter.AdapterDistrict;
import com.google.codelabs.mdc.java.shrine.entities.adapter.AdapterWard;
import com.google.codelabs.mdc.java.shrine.utils.Common;
import com.google.codelabs.mdc.java.shrine.utils.Constant;
import com.google.codelabs.mdc.java.shrine.utils.MyProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    List<City> cityList = new ArrayList<>();
    List<District> districtList = new ArrayList<>();
    List<Ward> wardList = new ArrayList<>();
    AutoCompleteTextView genderAutoCompleteTextView;
    AutoCompleteTextView yearAutoCompleteTextView;
    AutoCompleteTextView monthAutoCompleteTextView;
    AutoCompleteTextView dayAutoCompleteTextView;
    AutoCompleteTextView cityAutoCompleteTextView;
    AutoCompleteTextView districtAutoCompleteTextView;
    AutoCompleteTextView wardAutoCompleteTextView;
    MaterialButton loginButton;
    MaterialButton registerButton;
    TextInputEditText emailContentEditText;
    TextInputEditText firstNameContentEditText;
    TextInputEditText lastNameContentEditText;
    TextInputEditText phoneContentEditText;
    TextInputEditText passContentEditText;
    TextInputEditText rePassContentEditText;
    TextInputLayout emailInputLayOut;
    TextInputLayout firstNameInputLayOut;
    TextInputLayout lastNameInputLayOut;
    TextInputLayout yearInputLayOut;
    TextInputLayout monthInputLayOut;
    TextInputLayout dayInputLayOut;
    TextInputLayout cityInputLayOut;
    TextInputLayout districtInputLayOut;
    TextInputLayout wardInputLayOut;
    TextInputLayout genderInputLayOut;
    TextInputLayout phoneInputLayOut;
    TextInputLayout passInputLayOut;
    TextInputLayout rePassInputLayOut;

    MyProgressDialog myProgressDialog;

    RegisterForm registerForm;
    private Integer districtId;
    private Integer cityId;
    private Integer wardId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
        genderAutoCompleteTextView = view.findViewById(R.id.content_select_gender);
        yearAutoCompleteTextView = view.findViewById(R.id.content_select_birthday_year);
        monthAutoCompleteTextView = view.findViewById(R.id.content_select_birthday_month);
        dayAutoCompleteTextView = view.findViewById(R.id.content_select_birthday_day);
        cityAutoCompleteTextView = view.findViewById(R.id.content_select_city);
        districtAutoCompleteTextView = view.findViewById(R.id.content_select_district);
        wardAutoCompleteTextView = view.findViewById(R.id.content_select_ward);
        emailContentEditText = view.findViewById(R.id.content_email_text_input);
        firstNameContentEditText = view.findViewById(R.id.content_first_name_text_input);
        lastNameContentEditText = view.findViewById(R.id.content_last_name_text_input);
        phoneContentEditText = view.findViewById(R.id.content_phone_text_input);
        passContentEditText = view.findViewById(R.id.content_password_text_input);
        rePassContentEditText = view.findViewById(R.id.content_re_password_text_input);

        emailInputLayOut = view.findViewById(R.id.email_text_input);
        firstNameInputLayOut = view.findViewById(R.id.first_name_text_input);
        lastNameInputLayOut = view.findViewById(R.id.last_name_text_input);
        yearInputLayOut = view.findViewById(R.id.select_birthday_year);
        monthInputLayOut = view.findViewById(R.id.select_birthday_month);
        dayInputLayOut = view.findViewById(R.id.select_birthday_day);
        cityInputLayOut = view.findViewById(R.id.select_city);
        districtInputLayOut = view.findViewById(R.id.select_district);
        wardInputLayOut = view.findViewById(R.id.select_ward);
        genderInputLayOut = view.findViewById(R.id.select_gender);
        phoneInputLayOut = view.findViewById(R.id.phone_text_input);
        passInputLayOut = view.findViewById(R.id.password_text_input);
        rePassInputLayOut = view.findViewById(R.id.re_password_text_input);

        myProgressDialog = new MyProgressDialog(getActivity());
        registerForm = new RegisterForm();

        onClickLoginButton();
        onClickRegisterButton();
        setStaticDefaultValue();
        callApiGetAllCity();
        return view;
    }

    private void setStaticDefaultValue(){
        List<String> items = new ArrayList<>(Arrays.asList("Nam", "N???", "Kh??c"));
        List<Integer> items1 = Common.getListYear();
        List<Integer> items2 = Common.getListMonth();
        List<Integer> items3 = Common.getListDay();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.item_adapter_register, items);
        ArrayAdapter<Integer> arrayAdapter1 = new ArrayAdapter<>(requireActivity(), R.layout.item_adapter_register, items1);
        ArrayAdapter<Integer> arrayAdapter2 = new ArrayAdapter<>(requireActivity(), R.layout.item_adapter_register, items2);
        ArrayAdapter<Integer> arrayAdapter3 = new ArrayAdapter<>(requireActivity(), R.layout.item_adapter_register, items3);

        genderAutoCompleteTextView.setAdapter(arrayAdapter);
        yearAutoCompleteTextView.setAdapter(arrayAdapter1);
        monthAutoCompleteTextView.setAdapter(arrayAdapter2);
        dayAutoCompleteTextView.setAdapter(arrayAdapter3);

    }


    private void callApiGetAllCity() {
        myProgressDialog.show();
        ApiService.apiService.findAllCity().enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                assert myResponse != null;
                if (myResponse.getMessage().equals(Constant.SUCCESS_MESSAGE_CALL_API)) {
                    Gson gson = Common.getMyGson();
                    String json = gson.toJson(myResponse.getData());
                    cityList = gson.fromJson(json, new TypeToken<List<City>>(){}.getType());
                    ArrayAdapter<City> arrayAdapter = new AdapterCity(requireActivity(), R.layout.item_adapter_register, cityList);
                    cityAutoCompleteTextView.setAdapter(arrayAdapter);
                    myProgressDialog.dismiss();
                    onClickCity();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Call Api fail", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
    }

    private void callApiFindDistrictByCityId(int cityId) {
        myProgressDialog.show();
        ApiService.apiService.findDistrictByCityId(cityId).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                assert myResponse != null;
                if (myResponse.getMessage().equals(Constant.SUCCESS_MESSAGE_CALL_API)) {
                    Gson gson = Common.getMyGson();
                    String json = gson.toJson(myResponse.getData());
                    districtList = gson.fromJson(json, new TypeToken<List<District>>(){}.getType());
                    ArrayAdapter<District> arrayAdapter = new AdapterDistrict(requireActivity(), R.layout.item_adapter_register, districtList);
                    districtAutoCompleteTextView.setAdapter(arrayAdapter);
                    myProgressDialog.dismiss();
                    onClickDistrict();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Call Api fail", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
    }

    private void callApiFindWardByDistrictId(int districtId) {
        myProgressDialog.show();
        ApiService.apiService.findWardByDistrictId(districtId).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                MyResponse myResponse = response.body();
                assert myResponse != null;
                if (myResponse.getMessage().equals(Constant.SUCCESS_MESSAGE_CALL_API)) {
                    Gson gson = Common.getMyGson();
                    String json = gson.toJson(myResponse.getData());
                    wardList = gson.fromJson(json, new TypeToken<List<Ward>>(){}.getType());
                    ArrayAdapter<Ward> arrayAdapter = new AdapterWard(requireActivity(), R.layout.item_adapter_register, wardList);
                    wardAutoCompleteTextView.setAdapter(arrayAdapter);
                    myProgressDialog.dismiss();
                    onClickWard();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Call Api fail", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
    }

    private void onClickLoginButton(){
        loginButton.setOnClickListener(view1 -> {
            ((MainActivity)requireActivity()).switchLoginFragment();
        });
    }




    private void onClickCity(){
        cityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City city = (City) adapterView.getItemAtPosition(i);
                cityId = city.getId();
                districtAutoCompleteTextView.setText("");
                wardAutoCompleteTextView.setText("");
                callApiFindDistrictByCityId(city.getId());
            }
        });
    }

    private void onClickDistrict(){
        districtAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                District district = (District) adapterView.getItemAtPosition(i);
                districtId = district.getId();
                wardAutoCompleteTextView.setText("");
                callApiFindWardByDistrictId(district.getId());
            }
        });
    }

    private void onClickWard(){
        wardAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ward ward = (Ward) adapterView.getItemAtPosition(i);
                wardId = ward.getId();
            }
        });
    }

    private void onClickRegisterButton(){
        registerButton.setOnClickListener(view1 -> {
            try {
                System.out.println("abcabc");
                boolean check = validateRegisterForm();
                if(check){
                    RegisterForm registerForm = new RegisterForm();
                    registerForm.setEmail(emailContentEditText.getText().toString());
                    registerForm.setFirstname(firstNameContentEditText.getText().toString());
                    registerForm.setLastname(lastNameContentEditText.getText().toString());
                    Date birthday = Common.integerToDate(yearAutoCompleteTextView.getText().toString()
                            ,monthAutoCompleteTextView.getText().toString()
                            ,dayAutoCompleteTextView.getText().toString());
                    registerForm.setBirthday(birthday);
                    registerForm.setCityId(cityId);
                    registerForm.setDistrictId(districtId);
                    registerForm.setWardId(wardId);
                    registerForm.setGender(genderAutoCompleteTextView.getText().toString());
                    registerForm.setPhone(phoneContentEditText.getText().toString());
                    registerForm.setPassword(passContentEditText.getText().toString());
                    registerForm.setRePassword(rePassContentEditText.getText().toString());
                    callApiRegister(registerForm);
//
//                    Toast.makeText(getActivity(), "Check email ??i b???n ??i", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateRegisterForm(){
        boolean check = true;
        if("".equals(emailContentEditText.getText().toString())){
            emailInputLayOut.setError("Ch??a nh???p email");
            check = false;
        }else{
            emailInputLayOut.setError(null);
        }
        if("".equals(firstNameContentEditText.getText().toString())){
            firstNameInputLayOut.setError("Ch??a nh???p h??? v?? t??n ?????m");
            check = false;
        }else {
            firstNameInputLayOut.setError(null);
        }
        if("".equals(lastNameContentEditText.getText().toString())){
            lastNameInputLayOut.setError("Ch??a nh???p t??n");
            check = false;
        }else {
            lastNameInputLayOut.setError(null);
        }

        String year = yearAutoCompleteTextView.getText().toString();
        String month = monthAutoCompleteTextView.getText().toString();
        String day = dayAutoCompleteTextView.getText().toString();
        if("".equals(year)){
            yearInputLayOut.setError("Ch??a nh???p n??m sinh");
            check = false;
        }else {
            yearInputLayOut.setError(null);
        }
        if("".equals(month)){
            monthInputLayOut.setError("Ch??a nh???p th??ng sinh");
            check = false;
        }else {
            monthInputLayOut.setError(null);
        }
        if("".equals(day)){
            dayInputLayOut.setError("Ch??a nh???p ng??y sinh");
            check = false;
        }else {
            dayInputLayOut.setError(null);
        }

        if(!"".equals(year)
                && !"".equals(month)
                && !"".equals(day)){
            if(!Common.checkCorrectDate(year, month, day)){
                check = false;
                yearInputLayOut.setError("Ng??y sinh kh??ng ????ng");
                monthInputLayOut.setError(" ");
                dayInputLayOut.setError(" ");
            } else {
                yearInputLayOut.setError(null);
                monthInputLayOut.setError(null);
                dayInputLayOut.setError(null);
            }
        }

        if("".equals(cityAutoCompleteTextView.getText().toString())){
            cityInputLayOut.setError("Ch??a nh???p t???nh/th??nh ph???");
            check = false;
        }else {
            cityInputLayOut.setError(null);
        }
        if("".equals(districtAutoCompleteTextView.getText().toString())){
            districtInputLayOut.setError("Ch??a nh???p qu???n/huy???n");
            check = false;
        }else {
            districtInputLayOut.setError(null);
        }
        if("".equals(wardAutoCompleteTextView.getText().toString())){
            wardInputLayOut.setError("Ch??a nh???p ph?????ng/x??");
            check = false;
        }else {
            wardInputLayOut.setError(null);
        }
        if("".equals(genderAutoCompleteTextView.getText().toString())){
            genderInputLayOut.setError("Ch??a nh???p gi???i t??nh");
            check = false;
        }else {
            genderInputLayOut.setError(null);
        }

        if("".equals(phoneContentEditText.getText().toString())){
            phoneInputLayOut.setError("Ch??a nh???p s??? ??i???n tho???i");
            check = false;
        }else {
            phoneInputLayOut.setError(null);
        }

        if("".equals(passContentEditText.getText().toString())){
            passInputLayOut.setError("Ch??a nh???p m???t kh???u");
            check = false;
        }else {
            passInputLayOut.setError(null);
        }

        if("".equals(rePassContentEditText.getText().toString())){
            rePassInputLayOut.setError("Ch??a nh???p l???i m???t kh???u");
            check = false;
        }else {
            rePassInputLayOut.setError(null);
        }

        if(!"".equals(passContentEditText.getText().toString())
                && !passContentEditText.getText().toString().equals(rePassContentEditText.getText().toString())){

            passInputLayOut.setError("M???t kh???u v?? m???t kh???u nh???p l???i kh??ng tr??ng kh???p");
            rePassInputLayOut.setError(null);
            check = false;
        }else {
            passInputLayOut.setError(null);
            rePassInputLayOut.setError(null);
        }
        return check;
    }

    private void callApiRegister(RegisterForm registerForm) {
        myProgressDialog.show();
        ApiService.apiService.register(registerForm).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {

                MyResponse myResponse = response.body();
                assert myResponse != null;

                if(myResponse.getMessage().equals(Constant.SUCCESS_MESSAGE_CALL_API)){
                    ((MainActivity) requireActivity()).switchActiveFragment();
                }else{
                    Gson gson = Common.getMyGson();
                    String resultActive = gson.toJson(myResponse.getData());
                    switch (resultActive){
                        case Constant.REGISTER_RESULT_FAIL_1:
                            genderInputLayOut.setError(resultActive);
                        case Constant.REGISTER_RESULT_FAIL_2:
                            emailInputLayOut.setError(resultActive);
                        case Constant.REGISTER_RESULT_FAIL_3:
                            emailInputLayOut.setError(resultActive);
                    }
                }
                Toast.makeText(getActivity(),myResponse.toString(),Toast.LENGTH_SHORT).show();

                myProgressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(),"Error call api register",Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
    }
}
