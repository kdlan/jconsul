package com.loukou.jconsul.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.loukou.jconsul.client.model.Agent;
import com.loukou.jconsul.client.model.HealthCheck;
import com.loukou.jconsul.client.model.Member;
import com.loukou.jconsul.client.model.Service;

public class AgentRequestBuilder extends JConsulRequestBuilder {

    AgentRequestBuilder(RequestProcessor processor) {
        super(processor);
    }

    @SuppressWarnings("serial")
    public Map<String, HealthCheck> checks() {
        return getJsonResult("GET", "/agent/checks", new TypeToken<Map<String, HealthCheck>>() {
        });
    }

    @SuppressWarnings("serial")
    public Map<String, Service> services() {
        return getJsonResult("GET", "/agent/services", new TypeToken<Map<String, Service>>() {
        });
    }

    @SuppressWarnings("serial")
    public List<Member> members() {
        return getJsonResult("GET", "/agent/members", new TypeToken<List<Member>>() {
        });
    }

    public List<Member> members(boolean wan) {
        addParameter("wan", wan);
        return members();
    }

    public Agent self() {
        return getJsonResult("GET", "/agent/self", Agent.class);
    }

    public void maintenance(boolean enable) {
        addParameter("enable", enable);
        getPlainResult("PUT", "/agent/maintenance");
    }

    public void maintenance(boolean enable, String reason) {
        addParameter("reason", reason);
        maintenance(enable);
    }

    public void serviceMaintenance(String serviceId, boolean enable) {
        addParameter("enable", enable);
        getPlainResult("PUT", "/agent/service/maintenance/" + serviceId);
    }

    public void serviceMaintenance(String serviceId, boolean enable, String reason) {
        addParameter("reason", reason);
        serviceMaintenance(serviceId, enable);
    }

    public CheckRegisterBuilder registerCheck(String name) {
        return new CheckRegisterBuilder(name);
    }

    public ServiceRegisterBuilder registerService(String name) {
        return new ServiceRegisterBuilder(name);
    }

    public RegisterStatus wrapService(String serviceId) {
        return new ServiceStatus(serviceId);
    }

    public class CheckRegisterBuilder {
        private final Map<String, String> map = Maps.newHashMap();

        private CheckRegisterBuilder(String name) {
            map.put("Name", name);
        }

        public CheckRegisterBuilder id(String id) {
            map.put("ID", id);
            return this;
        }

        public CheckRegisterBuilder notes(String notes) {
            map.put("Notes", notes);
            return this;
        }

        public CheckRegisterBuilder service(String serviceId) {
            map.put("ServiceID", serviceId);
            return this;
        }

        private RegisterStatus execute() {
            setJsonBody(map);
            getPlainResult("PUT", "/agent/check/register");
            String checkId = map.get("ID");
            if (checkId == null || "".equals(checkId.trim())) {
                checkId = map.get("Name");
            }
            return new CheckStatus(checkId);
        }

        public RegisterStatus ttl(int ttl) {
            map.put("TTL", ttl + "s");
            return execute();
        }

        public RegisterStatus script(String script, int interval) {
            map.put("Script", script);
            map.put("Interval", interval + "s");
            return execute();
        }

        public RegisterStatus http(String url, int interval) {
            map.put("HTTP", url);
            map.put("Interval", interval + "s");
            return execute();
        }

        public RegisterStatus http(String url, int timeout, int interval) {
            map.put("Timeout", timeout + "s");
            return http(url, interval);
        }

    }

    public class ServiceRegisterBuilder {
        private final Map<String, Object> map = Maps.newHashMap();

        private ServiceRegisterBuilder(String name) {
            map.put("Name", name);
        }

        public ServiceRegisterBuilder id(String id) {
            map.put("ID", id);
            return this;
        }

        public ServiceRegisterBuilder tags(String... tags) {
            map.put("Tags", Arrays.asList(tags));
            return this;
        }

        public ServiceRegisterBuilder port(int port) {
            map.put("Port", port);
            return this;
        }

        public ServiceRegisterBuilder address(String address) {
            map.put("Address", address);
            return this;
        }

        public RegisterStatus ttl(int ttl) {
            map.put("Check", ImmutableMap.of("TTL", ttl + "s"));
            return new ServiceStatus(getId());
        }

        public RegisterStatus script(String script, int interval) {

            map.put("Check", ImmutableMap.of("Script", script, "Interval", interval + "s"));
            return new ServiceStatus(getId());
        }

        public RegisterStatus http(String url, int interval) {
            map.put("Check", ImmutableMap.of("HTTP", url, "Interval", interval + "s"));
            return new ServiceStatus(getId());
        }

        public RegisterStatus http(String url, int timeout, int interval) {
            map.put("Check", ImmutableMap.of("HTTP", url, "Timeout", timeout + "s", "Interval", interval + "s"));
            return new ServiceStatus(getId());
        }

        public RegisterStatus execute() {
            setJsonBody(map);
            getPlainResult("PUT", "/agent/service/register");
            return new ServiceWithoutCheckStatus(getId());
        }

        private String getId() {
            String id = (String) map.get("ID");
            if (id == null || "".equals(id.trim())) {
                id = (String) map.get("Name");
            }
            return id;
        }

    }

    public interface RegisterStatus {
        public void deregister();

        public void pass();

        public void pass(String note);

        public void warn();

        public void warn(String note);

        public void fail();

        public void fail(String note);
    }

    public class CheckStatus implements RegisterStatus {
        private final String checkId;

        protected CheckStatus(String checkId) {
            this.checkId = checkId;
        }

        public void deregister() {
            getPlainResult("GET", "/agent/check/deregister/" + checkId);
        }

        private void status(String status, String note) {
            if (note != null) {
                addParameter("note", note);
            }
            getPlainResult("GET", "/agent/check/" + status + "/" + checkId);
        }

        public void pass() {
            status("pass", null);
        }

        public void pass(String note) {
            status("pass", note);
        }

        public void warn() {
            status("warn", null);
        }

        public void warn(String note) {
            status("warn", note);
        }

        public void fail() {
            status("fail", null);
        }

        public void fail(String note) {
            status("fail", note);
        }

    }

    private class ServiceStatus extends CheckStatus {

        private final String serviceId;

        public ServiceStatus(String serviceId) {
            super("service:" + serviceId);
            this.serviceId = serviceId;
        }

        @Override
        public void deregister() {
            getPlainResult("GET", "/agent/service/deregister/" + serviceId);
        }

    }

    private class ServiceWithoutCheckStatus implements RegisterStatus {

        private final String serviceId;

        private ServiceWithoutCheckStatus(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public void pass() {
        }

        @Override
        public void pass(String note) {
        }

        @Override
        public void warn() {
        }

        @Override
        public void warn(String note) {
        }

        @Override
        public void fail() {
        }

        @Override
        public void fail(String note) {
        }

        @Override
        public void deregister() {
            getPlainResult("GET", "/agent/service/deregister/" + serviceId);
        }

    }
}
