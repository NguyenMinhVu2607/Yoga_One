package com.at17.kma.yogaone.Fragment_Coach;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ak.ColoredDate;
import com.ak.KalendarView;
import com.at17.kma.yogaone.Adapter.ClassAdapter;
import com.at17.kma.yogaone.ModelClassInfo.ClassInfo;
import com.at17.kma.yogaone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeCoachFragment extends Fragment {

    private KalendarView kalendarView;
    private ListView listView;
    private List<ClassInfo> classList;
    private ArrayAdapter<String> listViewAdapter;
    private SparseBooleanArray markedDays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);
        kalendarView = view.findViewById(R.id.kalendarView);
        listView = view.findViewById(R.id.listViewClasses);
        kalendarView.setInitialSelectedDate(new Date());


        // Khởi tạo danh sách lớp học và biểu đồ
        classList = new ArrayList<>();
        markedDays = new SparseBooleanArray();

        // Khởi tạo ArrayAdapter cho ListView
        listViewAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), ""+i, Toast.LENGTH_SHORT).show();
            }
        });
        // Xử lý sự kiện khi người dùng chọn ngày trên KalendarView
        kalendarView.setDateSelector(selectedDate -> {
            Log.d("DateSel", selectedDate.toString());
            // Do whatever you want with the clicked date
            // Hiển thị danh sách lớp học cho ngày đã chọn
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            displayClassesForSelectedDay(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        });
        queryClassesFromFirestore();
        return view;

    }
    private void queryClassesFromFirestore() {
        // Truy vấn dữ liệu từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("classes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Chuyển đổi dữ liệu từ Firestore thành đối tượng ClassInfo
                            ClassInfo classInfo = document.toObject(ClassInfo.class);
                            classList.add(classInfo);

                            // Đánh dấu ngày có lịch học
                            markDaysWithClasses();
                        }
                        // Hiển thị lịch và danh sách lớp học cho ngày mặc định (hiện tại)
                        displayClassesForSelectedDay(
                                Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        );
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
    private void markDaysWithClasses() {
        List<ColoredDate> datesColors = new ArrayList<>();

        for (ClassInfo classInfo : classList) {
            long startTime = classInfo.getStartDay();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);

            // Lưu trữ thông tin ngày vào biểu đồ
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

            Log.d("Marked1", "Marking day: " + dayOfYear);

            markedDays.put(dayOfYear, true);

            // Thêm ngày đã có lịch học vào danh sách màu sắc
//            datesColors.add(new ColoredDate(calendar.getTime(), getResources().getColor(R.color.white)));
        }

        // Set danh sách màu sắc cho KalendarView
        kalendarView.setColoredDates(datesColors);
    }

    private void displayClassesForSelectedDay(int year, int month, int dayOfMonth) {
        // Xóa danh sách lớp học cũ
        listViewAdapter.clear();

        // Lấy ngày trong tuần của ngày đã chọn
        String selectedDayOfWeek = getDayOfWeek(year, month, dayOfMonth);

        // Lọc danh sách lớp học cho ngày đã chọn (theo dayOfWeek và khoảng thời gian)
        for (ClassInfo classInfo : classList) {
            List<String> classDaysOfWeek = classInfo.getDayOfWeek();

            // Kiểm tra xem lớp học diễn ra vào ngày đã chọn
            if (classDaysOfWeek.contains(selectedDayOfWeek)) {
                // Hiển thị thông tin lớp học trong ListView
                String className = classInfo.getClassName();
                String teacherName = classInfo.getTeacherName();
                String location = classInfo.getLocation();
                String startTimeClass = classInfo.getTimeStringStart();
                String endTimeClass = classInfo.getTimeStringEnd();

                // Lấy thông tin về thời gian của lớp học
                long startTimeMillis = classInfo.getStartDay();
                long endTimeMillis = classInfo.getEndDay();

                // Kiểm tra xem lớp học có diễn ra trong ngày đã chọn không
                if (isClassWithinSelectedDay(startTimeMillis, endTimeMillis, year, month, dayOfMonth)) {
                    // Chuyển đổi thời gian sang chuỗi ngày tháng năm
                    String startTimeFormatted = convertMillisToDate(startTimeMillis);
                    String endTimeFormatted = convertMillisToDate(endTimeMillis);

                    // Hiển thị thông tin lớp học trong ListView
                    listViewAdapter.add(
                            className + " - " + teacherName + "\n"
                                    + "Địa điểm: " + location + "\n"
                                    +  startTimeClass + " - " + endTimeClass + "\n"

                                    + "Ngày bắt đầu: " + startTimeFormatted + "\n"
                                    + "Ngày kết thúc: " + endTimeFormatted
                    );
                }
            }
        }
    }

    private boolean isClassWithinSelectedDay(long startTime, long endTime, int year, int month, int dayOfMonth) {
        // Tạo Calendar cho ngày bắt đầu và ngày kết thúc
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);

        // Tạo Calendar cho ngày đã chọn
        Calendar selectedDayCalendar = Calendar.getInstance();
        selectedDayCalendar.set(year, month, dayOfMonth, 0, 0, 0);

        // Kiểm tra xem lớp học có diễn ra trong ngày đã chọn không
        return selectedDayCalendar.getTimeInMillis() >= startCalendar.getTimeInMillis() &&
                selectedDayCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis();
    }

    private String convertMillisToDate(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return sdf.format(calendar.getTime());
    }

//    private boolean isClassWithinSelectedTime(ClassInfo classInfo, int year, int month, int dayOfMonth) {
//        // Lấy danh sách các ngày trong tuần mà lớp học diễn ra
//        List<String> daysOfWeek = classInfo.getDayOfWeek();
//
//        // Tạo Calendar cho ngày đã chọn
//        Calendar selectedDayCalendar = Calendar.getInstance();
//        selectedDayCalendar.set(year, month, dayOfMonth, 0, 0, 0);
//
//        // Kiểm tra xem ngày đã chọn có trùng với bất kỳ ngày nào trong danh sách ngày của lớp học không
//        for (String classDayOfWeek : daysOfWeek) {
//            // Chuyển đổi classDayOfWeek thành ngày của tuần trong Calendar
//            int classDay = convertDayOfWeek(classDayOfWeek);
//
//            // Kiểm tra xem ngày đã chọn có trùng với ngày của lớp học không
//            if (selectedDayCalendar.get(Calendar.DAY_OF_WEEK) == classDay) {
//                // Kiểm tra xem thời gian đã chọn có nằm trong khoảng thời gian của lớp học không
//                long startTime = classInfo.getStartDay();
//                long endTime = classInfo.getEndDay();
//                return selectedDayCalendar.getTimeInMillis() >= startTime && selectedDayCalendar.getTimeInMillis() <= endTime;
//            }
//        }
//
//        return false;
//    }

//    // Phương thức hỗ trợ để chuyển đổi ngày trong tuần (dưới dạng chuỗi) thành ngày của tuần trong Calendar
//    private int convertDayOfWeek(String dayOfWeek) {
//        switch (dayOfWeek) {
//            case "Chủ Nhật":
//                return Calendar.SUNDAY;
//            case "Thứ 2":
//                return Calendar.MONDAY;
//            case "Thứ 3":
//                return Calendar.TUESDAY;
//            case "Thứ 4":
//                return Calendar.WEDNESDAY;
//            case "Thứ 5":
//                return Calendar.THURSDAY;
//            case "Thứ 6":
//                return Calendar.FRIDAY;
//            case "Thứ 7":
//                return Calendar.SATURDAY;
//            default:
//                return -1; // Xử lý trường hợp ngày không hợp lệ
//        }
//    }

    // Phương thức để lấy dayOfWeek từ ngày đã chọn
    private String getDayOfWeek(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // Lấy ngày trong tuần dưới dạng văn bản (Chủ Nhật, Thứ 2, ..., Thứ 7)
        String[] daysOfWeek = {"Chủ Nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Trả về ngày trong tuần dưới dạng văn bản
        return daysOfWeek[dayOfWeek -1];
    }

    //

}