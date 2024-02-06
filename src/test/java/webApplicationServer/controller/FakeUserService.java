package webApplicationServer.controller;

import dto.UserLoginDto;
import dto.UserSignUpDto;
import service.UserService;
import session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class FakeUserService implements UserService {
    private final Map<String, InvocationInfo> methodInvocations = new HashMap<>();

    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        recordInvocation("signUp");
        // signUp 구현
    }

    @Override
    public Optional<UUID> login(UserLoginDto userLoginDto) {
        return Optional.of(UUID.randomUUID());
    }

    @Override
    public void logout(UUID sessionId) {
        Session.logout(sessionId);
    }

    // 다른 UserService 메서드들에 대한 빈 메서드를 추가

    private void recordInvocation(String methodName) {
        methodInvocations.computeIfAbsent(methodName, k -> new InvocationInfo()).recordInvocation();
    }

    public boolean wasMethodCalled(String methodName) {
        return methodInvocations.containsKey(methodName);
    }

    public int getMethodInvocationCount(String methodName) {
        return methodInvocations.getOrDefault(methodName, new InvocationInfo()).getInvocationCount();
    }

    private static class InvocationInfo {
        private int invocationCount = 0;

        public void recordInvocation() {
            invocationCount++;
        }

        public int getInvocationCount() {
            return invocationCount;
        }
    }
}
