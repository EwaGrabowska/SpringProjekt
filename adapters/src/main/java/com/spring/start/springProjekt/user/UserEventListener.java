package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    private final UserServiceForAdminFacade facade;

    public UserEventListener(final UserServiceForAdminFacade facade) {
        this.facade = facade;
    }


    @EventListener
    // warning: must be synchronous in current design
    public void on(ArgoFileEvent argoFileEvent) {
        facade.handle(argoFileEvent);
    }
}
