package com.example.meeting;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import com.example.library.helper.RxHelper;
import com.example.library.utils.LogUtils;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/28 下午8:28
 * desc   :
 * version: 1.0
 */
@RunWith(AndroidJUnit4.class)
public class PersonaTest {


    @Before
    public void setUp() {

    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
    }


    private void initRxJava() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }


    /***
     * 测试新增用户
     */
    @Test
    public void saveUser() {
        final User user = Mockito.spy(User.class);
        Mockito.when(user.getName()).thenReturn("xiongtao");
        Mockito.when(user.getNo()).thenReturn(1);
        Mockito.when(user.getCreateTime()).thenReturn(System.currentTimeMillis());
        Mockito.when(user.getIsDelete()).thenReturn(GlobalConstant.VALUE_IS_NOT_DELETE);
        Mockito.when(user.getIsSkip()).thenReturn(GlobalConstant.VALUE_IS_NOT_SKIP);
        //保存人员到数据库
        Long id = AppDatabase.getInstance().userDao().insertUsers(user);
        User targetUser = AppDatabase.getInstance().userDao().getUserByNo(user.getNo());
        assert user.getName() == targetUser.getName();
        //再根据刚才插入的id去数据库查询
    }


    @Test
    public void deletePerson() {
//        initRxJava();
          User user = AppDatabase.getInstance().userDao().getNewestNumber2();
//        TestSubscriber<User> subscriber = TestSubscriber.create();
//        obserable.subscribe((Consumer<? super User>) subscriber);
//        subscriber.assertNoErrors();
//        subscriber.assertComplete();
//        List<Object> objects = subscriber.getEvents().get(0);
    }
}
