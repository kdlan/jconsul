package com.loukou.jconsul.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.loukou.jconsul.client.model.agent.Agent;
import com.loukou.jconsul.client.model.agent.Check;
import com.loukou.jconsul.client.model.agent.Member;
import com.loukou.jconsul.client.model.agent.ServiceRegistration;
import com.loukou.jconsul.client.model.health.HealthCheck;
import com.loukou.jconsul.client.model.health.Service;
import com.google.common.reflect.TypeToken;

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

    public List<Member> membersWan() {
        addParameter("wan", true);
        return members();
    }

    public Agent self() {
        return getJsonResult("GET", "/agent/self", Agent.class);
    }

    public RegisterStatus registerCheck(Check check) {
        setJsonBody(check);
        getPlainResult("PUT", "/agent/check/register");
        String checkId = check.getId();
        if (checkId == null || "".equals(checkId.trim())) {
            checkId = check.getName();
        }
        return new CheckStatus(checkId);
    }

    public RegisterStatus registerScriptCheck(String checkId, String name, String script, int interval) {
        Check check = new Check();
        check.setId(checkId);
        check.setName(name);
        check.setScript(script);
        check.setInterval(interval + "s");
        return registerCheck(check);
    }

    public RegisterStatus registerTTLCheck(String checkId, String name, int ttl) {
        Check check = new Check();
        check.setId(name);
        check.setName(name);
        check.setTtl(ttl + "s");
        return registerCheck(check);
    }

    public RegisterStatus wrapCheck(String checkId) {
        return new CheckStatus(checkId);
    }

    public RegisterStatus registerService(ServiceRegistration service) {
        setJsonBody(service);
        getPlainResult("PUT", "/agent/service/register");
        String id = service.getId();
        if (id == null || "".equals(id.trim())) {
            id = service.getName();
        }
        return new ServiceStatus(id);
    }

    private RegisterStatus registerService(String id, String name, int port, ServiceRegistration.Check check,
            String... tags) {
        ServiceRegistration service = new ServiceRegistration();
        service.setId(id);
        service.setName(name);
        service.setPort(port);
        if (tags != null) {
            service.setTags(Arrays.asList(tags));
        }
        service.setCheck(check);

        return registerService(service);
    }

    public RegisterStatus registerService(String id, String name, int port, int ttl, String... tags) {

        ServiceRegistration.Check check = new ServiceRegistration.Check();
        check.setTtl(ttl + "s");

        return registerService(id, name, port, check, tags);
    }

    public RegisterStatus registerService(String id, String name, int port, String script, int interval, String... tags) {
        ServiceRegistration.Check check = new ServiceRegistration.Check();
        check.setScript(script);
        check.setInterval(interval + "s");
        return registerService(id, name, port, check, tags);
    }

    public RegisterStatus wrapService(String serviceId) {
        return new ServiceStatus(serviceId);
    }

    public interface RegisterStatus{
        public void deregister() ;

        public void pass() ;
        public void pass(String note) ;
        public void warn() ;

        public void warn(String note) ;
        public void fail() ;

        public void fail(String note) ;
    }

    public class CheckStatus implements RegisterStatus{
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
}
