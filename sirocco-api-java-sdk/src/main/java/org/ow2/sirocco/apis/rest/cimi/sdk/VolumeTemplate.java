package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiVolumeTemplate;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiVolumeTemplateCollection;
import org.ow2.sirocco.apis.rest.cimi.utils.ConstantsPath;

public class VolumeTemplate extends Resource<CimiVolumeTemplate> {
    private VolumeImage volumeImage;

    private VolumeConfiguration volumeConfig;

    public VolumeTemplate() {
        super(null, new CimiVolumeTemplate());
    }

    public VolumeTemplate(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiVolumeTemplate());
        this.cimiObject.setHref(id);
        this.cimiObject.setId(id);
    }

    public VolumeTemplate(final CimiClient cimiClient, final CimiVolumeTemplate cimiObject) {
        super(cimiClient, cimiObject);
        this.volumeImage = new VolumeImage(cimiClient, cimiObject.getVolumeImage());
        this.volumeConfig = new VolumeConfiguration(cimiClient, cimiObject.getVolumeConfig());
    }

    public VolumeImage getVolumeImage() {
        return this.volumeImage;
    }

    public void setVolumeImage(final VolumeImage volumeImage) {
        this.volumeImage = volumeImage;
        this.cimiObject.setVolumeImage(volumeImage.cimiObject);
    }

    public VolumeConfiguration getVolumeConfig() {
        return this.volumeConfig;
    }

    public void setVolumeConfig(final VolumeConfiguration volumeConfig) {
        this.volumeConfig = volumeConfig;
        this.cimiObject.setVolumeConfig(volumeConfig.cimiObject);
    }

    public void delete() throws CimiException {
        this.cimiClient.deleteRequest(this.cimiClient.extractPath(this.getId()));
    }

    public static VolumeTemplate createVolumeTemplate(final CimiClient client, final VolumeTemplate volumeTemplate)
        throws CimiException {
        CimiVolumeTemplate cimiObject = client.postRequest(ConstantsPath.VOLUME_TEMPLATE_PATH, volumeTemplate.cimiObject,
            CimiVolumeTemplate.class);
        return new VolumeTemplate(client, cimiObject);
    }

    public static List<VolumeTemplate> getVolumeTemplates(final CimiClient client) throws CimiException {
        CimiVolumeTemplateCollection volumeTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeTemplates().getHref()), CimiVolumeTemplateCollection.class);

        List<VolumeTemplate> result = new ArrayList<VolumeTemplate>();

        if (volumeTemplateCollection.getCollection() != null) {
            for (CimiVolumeTemplate cimiVolumeTemplate : volumeTemplateCollection.getCollection().getArray()) {
                result.add(VolumeTemplate.getVolumeTemplateByReference(client, cimiVolumeTemplate.getHref()));
            }
        }
        return result;
    }

    public static VolumeTemplate getVolumeTemplateByReference(final CimiClient client, final String ref) throws CimiException {
        return new VolumeTemplate(client, client.getCimiObjectByReference(ref, CimiVolumeTemplate.class));
    }

    public static VolumeTemplate getVolumeTemplateById(final CimiClient client, final String id) throws CimiException {
        String path = client.getVolumeTemplatesPath() + "/" + id;
        return new VolumeTemplate(client, client.getCimiObjectByReference(path, CimiVolumeTemplate.class));
    }

}
