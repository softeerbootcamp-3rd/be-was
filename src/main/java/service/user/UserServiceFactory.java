package service.user;

import service.Service;
import service.ServiceFactory;
import service.ServiceMapper;

public class UserServiceFactory implements ServiceFactory {

    // Thread-Safe 한 싱글톤 객체화
    private UserServiceFactory() {
    }

    private static class SingleInstanceHolder {

        private static final UserServiceFactory INSTANCE = new UserServiceFactory();
    }

    public static UserServiceFactory getInstance() {
        return UserServiceFactory.SingleInstanceHolder.INSTANCE;
    }

    @Override
    public Service createService(String url) {
        Class<? extends Service> serviceClass = ServiceMapper.getServiceClass(url);

        if (serviceClass != null) {
            try {
                return serviceClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create service", e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported URL: " + url);
        }
    }

}
