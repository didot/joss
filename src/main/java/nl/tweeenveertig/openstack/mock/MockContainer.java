package nl.tweeenveertig.openstack.mock;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.http.HttpStatus;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class MockContainer extends AbstractMock<ContainerInformation>{

    private Map<StoredObject, MockObject> objects = new TreeMap<StoredObject, MockObject>();

    private boolean publicContainer = false;

    public MockObject getOrCreateObject(StoredObject object) {
        MockObject foundObject = objects.get(object);
        if (foundObject == null) {
            foundObject = new MockObject();
            objects.put(object, foundObject);
        }
        return foundObject;
    }

    public MockObject getObject(StoredObject object) {
        MockObject foundObject = objects.get(object);
        if (foundObject == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
        }
        return foundObject;
    }

    public int getNumberOfObjects() {
        return objects.size();
    }

    public void makeContainerPublic() {
        this.publicContainer = true;
    }

    public void makeContainerPrivate() {
        this.publicContainer = false;
    }

    @Override
    protected void appendInformation(ContainerInformation info) {
        info.setPublicContainer(this.publicContainer);
        int numberOfObjects = 0;
        long numberOfBytes = 0;
        for (MockObject object : objects.values()) {
            numberOfObjects++;
            numberOfBytes += object.getInfo().getContentLength();
        }
        info.setObjectCount(numberOfObjects);
        info.setBytesUsed(numberOfBytes);
    }

    @Override
    protected ContainerInformation createInformationContainer() {
        return new ContainerInformation();
    }

    public Collection<StoredObject> listObjects() {
        return objects.keySet();
    }

    public void deleteObject(StoredObject object) {
        getObject(object); // check for existence
        objects.remove(object);
    }
}
