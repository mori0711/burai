/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.graph;

import java.io.File;
import java.io.IOException;

import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.result.QEFXResultButtonWrapper;
import burai.project.Project;
import burai.project.property.ProjectGeometryList;
import burai.project.property.ProjectProperty;

public class QEFXOptForceButton extends QEFXGraphButton<QEFXForceViewer> {

    private static final String FILE_NAME = ".burai.graph.opt.force";

    private static final String BUTTON_TITLE = "OPT";
    private static final String BUTTON_SUBTITLE = ".force";
    private static final String BUTTON_FONT_COLOR = "-fx-text-fill: derive(mediumorchid, -15.0%)";
    private static final String BUTTON_BACKGROUND = "-fx-background-color: snow";

    public static QEFXResultButtonWrapper<QEFXOptForceButton> getWrapper(QEFXProjectController projectController, Project project) {

        ProjectProperty projectProperty = project == null ? null : project.getProperty();
        if (projectProperty == null) {
            return null;
        }

        ProjectGeometryList projectGeometryList = projectProperty.getOptList();
        if (projectGeometryList == null || projectGeometryList.numGeometries() < 1) {
            return null;
        }

        if (!projectGeometryList.hasAnyConvergedGeometries()) {
            return null;
        }

        return () -> {
            QEFXOptForceButton button = new QEFXOptForceButton(projectController, projectProperty);

            String propPath = project == null ? null : project.getDirectoryPath();
            File propFile = propPath == null ? null : new File(propPath, FILE_NAME);
            if (propFile != null) {
                button.setPropertyFile(propFile);
            }

            return button;
        };
    }

    private ProjectProperty projectProperty;

    private QEFXOptForceButton(QEFXProjectController projectController, ProjectProperty projectProperty) {
        super(projectController, BUTTON_TITLE, BUTTON_SUBTITLE);

        if (projectProperty == null) {
            throw new IllegalArgumentException("projectProperty is null.");
        }

        this.projectProperty = projectProperty;

        this.setIconStyle(BUTTON_BACKGROUND);
        this.setLabelStyle(BUTTON_FONT_COLOR);
    }

    @Override
    protected QEFXForceViewer createGraphViewer() throws IOException {
        if (this.projectController == null) {
            return null;
        }

        return new QEFXForceViewer(this.projectController, this.projectProperty, false);
    }
}
