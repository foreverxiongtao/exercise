package com.example.meeting;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.example.meeting.constant.GlobalConstant;
import com.example.meeting.db.AppDatabase;
import com.example.meeting.model.entity.User;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * author : desperado
 * e-mail : foreverxiongtao@sina.com
 * date   : 2019/4/28 下午8:28
 * desc   :
 * version: 1.0
 */
@RunWith(AndroidJUnit4.class)
public class PersonTest {


    @Before
    public void setUp() {

    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
    }


    /***
     * 初始化rxJava
     */
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
        User user = createUser("xiongtao", 1);
        //保存人员到数据库
        Long id = AppDatabase.getInstance().userDao().insertUsers(user);
        //再根据刚才插入的id去数据库查询
        User targetUser = AppDatabase.getInstance().userDao().getUserByNo(user.getNo());
        //相等则保存成功
        assert user.getName() == targetUser.getName();
    }


    /***
     * 创建用户
     * @param name
     * @param no
     * @return
     */
    public User createUser(String name, int no) {
        final User user = Mockito.mock(User.class);
        Mockito.when(user.getName()).thenReturn(name);
        Mockito.when(user.getNo()).thenReturn(no);
        Mockito.when(user.getCreateTime()).thenReturn(System.currentTimeMillis());
        Mockito.when(user.getIsDelete()).thenReturn(GlobalConstant.VALUE_IS_NOT_DELETE);
        Mockito.when(user.getIsSkip()).thenReturn(GlobalConstant.VALUE_IS_NOT_SKIP);
        return user;
    }


    /***
     * 测试用户删除
     */
    @Test
    public void deletePerson() {
        User user = createUser("xiongtao", 1);
        //保存用户到数据库
        long row1 = AppDatabase.getInstance().userDao().insertUsers(user);
        assert row1 > 0;
        Mockito.when(user.getId()).thenReturn((int) row1);  //设置刚才的用户id
        //设置当前用户的删除状态为删除
        Mockito.when(user.getIsDelete()).thenReturn(GlobalConstant.VALUE_IS_DELETE);
        //修改数据库的删除状态
        row1 = AppDatabase.getInstance().userDao().updateUser(user);
        assert row1 > 0;
        User targetUser = AppDatabase.getInstance().userDao().getUserByNo(user.getNo());

        assert targetUser.getIsDelete() == user.getIsDelete();
    }


    /***
     * 跳过某一个人功能
     */
    @Test
    public void skip() {
        User user = createUser("xiongtao", 1);
        //保存用户到数据库
        long row1 = AppDatabase.getInstance().userDao().insertUsers(user);
        assert row1 > 0;
        Mockito.when(user.getId()).thenReturn((int) row1);  //设置刚才的用户id
        Mockito.when(user.getIsSkip()).thenReturn(GlobalConstant.VALUE_IS_SKIP); //设置当前的用户为已跳过
        row1 = AppDatabase.getInstance().userDao().updateUser(user);
        assert row1 > 0;
        User targetUser = AppDatabase.getInstance().userDao().getUserByNo(user.getNo());
        assert targetUser.getIsSkip() == user.getIsSkip();
    }
}
