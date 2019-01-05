package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.NotificationDto;
import ru.kpfu.itis.androidlab.Join.form.AnswerNotificationForm;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.NotificationRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;

import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationServiceInt {

//    private static final String INVITE = "Пользователь %s приглашает вас присоедениться к проекту %s";                  //0
//    private static final String JOIN = "Пользователь %s хочет присоединиться к проекту %s";                             //2
//    private static final String YES_FROM_USER = "Пользователь %s принял ваше приглашение на вступление в проект %s";    //1
//    private static final String NO_FROM_USER = "Пользователь %s отклонил ваше приглашение на всупление в проект %s";    //1
//    private static final String YES_FROM_PROJECT = "Пользователь %s принял вашу заявку на вступление в проект %s";      //3
//    private static final String NO_FROM_PROJECT = "Пользователь %s отклонил вашу заявку на вступление в проект %s";     //3
//    private static final String YES_JOIN = "Вы приняли пользователя %s в проект %s";                                    //5
//    private static final String NO_JOIN = "Вы откланили заювку пользователя %s на вступление в проект %s";              //5
//    private static final String YES_INVITE = "Вы приняли приглашение на вступление в проект %s";                        //4
//    private static final String NO_INVITE = "Вы откланили приглашение на всупление в проект %s";                        //4

    private NotificationRepository notificationRepository;
    private ProjectServiceInt projectService;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   ProjectServiceInt projectService) {
        this.notificationRepository = notificationRepository;
        this.projectService = projectService;
    }

    @Override
    public void addNotification(User user, Project project, int type)
    {
        Notification notification = Notification.builder().date(new Date())
                .project(project)
                .user(user)
                .type(type)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void answerNotification(Long id, AnswerNotificationForm answerNotificationForm) {
        Notification notification = notificationRepository.getOne(id);
        User user = notification.getUser();
        Project project = notification.getProject();

        Notification newNotification = Notification.builder().project(project)
                                            .user(user)
                                            .date(new Date())
                                            .build();


        if (notification.getType() == 0) {
            newNotification.setType(1);
            notification.setType(4);
            if (answerNotificationForm.isAnswer()) {
                projectService.addUserToProject(user, project);
            }
        }
        else if (notification.getType() == 2) {
            newNotification.setType(3);
            notification.setType(5);
            if (answerNotificationForm.isAnswer()) {
                projectService.addUserToProject(user, project);
            }
        }

        notificationRepository.save(notification);
        notificationRepository.save(newNotification);
    }

    @Override
    public List<NotificationDto> getUserNotificationDtos(User user) {
        List<Notification> notifications = getUserNotification(user);
        List<NotificationDto> notificationDtos = new ArrayList<>();
        if (notifications != null) {
            for (Notification notification: notifications) {
                notificationDtos.add(NotificationDto.from(notification));
            }
        }

        return notificationDtos;
    }

    @Override
    public List<User> getInvitedUser(Long projectId){
        List<Notification> notifications = notificationRepository.findAllByTypeAndProject(0, projectService.getProject(projectId));
        //TODO more clever finding, may be in db do this get??
        List<User> users = new ArrayList<>();
        for (Notification notification: notifications) {
            users.add(notification.getUser());
        }

        return users;
    }

    @Override
    public List<User> getJoinedUser(Long projectId) {
        List<Notification> notifications = notificationRepository.findAllByTypeAndProject(2, projectService.getProject(projectId));
        List<User> users = new ArrayList<>();
        for (Notification notification: notifications) {
            users.add(notification.getUser());
        }
        return users;
    }

    @Override
    public List<Project> getProjectUserJoined(User user) {
        List<Notification> notifications = notificationRepository.findAllByTypeAndUser(2, user);
        List<Project> projects = new ArrayList<>();
        for (Notification notification: notifications) {
            projects.add(notification.getProject());
        }

        return projects;
    }

    @Override
    public List<Project> getProjectUserInvited(User user) {
        List<Notification> notifications = notificationRepository.findAllByTypeAndUser(0, user);
        List<Project> projects = new ArrayList<>();
        for (Notification notification: notifications) {
            projects.add(notification.getProject());
        }

        return projects;
    }

    private List<Notification> getUserNotification(User user) {
        List<Project> projects = projectService.getUserOwnerProjects(user);
        List<Notification> notifications = new ArrayList<>();
        List<Notification> buf = notificationRepository.findAllByTypeInAndUser(Arrays.asList(0,2,4), user);
        if (buf != null) notifications.addAll(buf);
        buf = notificationRepository.findAllByTypeInAndProjectIn(Arrays.asList(1,3,5),projects);
        if (buf != null) notifications.addAll(buf);

        return notifications;
    }

//    private Notification createNotification(String text, )
}
