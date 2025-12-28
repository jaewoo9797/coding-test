package com.seowon.coding.domain.model;


import com.seowon.coding.util.ListFun;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

class PermissionChecker {

    /**
     * TODO #7: 코드를 최적화하세요
     * 테스트 코드`PermissionCheckerTest`를 활용하시면 리펙토링에 도움이 됩니다.
     */
    public static boolean hasPermission(
            String userId,
            String targetResource,
            String targetAction,
            List<User> users,
            List<UserGroup> groups,
            List<Policy> policies
    ) {
        Map<String, User> userMap = ListFun.toHashMap(users, User::getId);
        User foundUser = userMap.get(userId);
        if (foundUser == null) {
            return false;
        }

        Map<String, UserGroup> groupMap = ListFun.toHashMap(groups, UserGroup::getId);
        Map<String, Policy> policyMap = ListFun.toHashMap(policies, Policy::getId);

        // user와 연결된 group의 id 들을 이용해서 policy 찾아 연결
        for (String groupId : foundUser.groupIds) {
            UserGroup group = groupMap.get(groupId);
            if (group == null) continue; // 유저가 가진 groupId가 목록에 없으면 스킵

            for (String policyId : group.policyIds) {
                Policy policy = policyMap.get(policyId);
                if (policy == null) continue;

                for (Statement statement : policy.statements) {
                    if (statement.actions.contains(targetAction)
                            && statement.resources.contains(targetResource)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

class User {
    @Getter
    String id;
    List<String> groupIds;

    public User(String id, List<String> groupIds) {
        this.id = id;
        this.groupIds = groupIds;
    }
}

class UserGroup {
    @Getter
    String id;
    List<String> policyIds;

    public UserGroup(String id, List<String> policyIds) {
        this.id = id;
        this.policyIds = policyIds;
    }
}

class Policy {
    @Getter
    String id;
    List<Statement> statements;

    public Policy(String id, List<Statement> statements) {
        this.id = id;
        this.statements = statements;
    }
}

class Statement {
    List<String> actions;
    List<String> resources;

    @Builder
    public Statement(List<String> actions, List<String> resources) {
        this.actions = actions;
        this.resources = resources;
    }
}