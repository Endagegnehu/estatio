/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.module.application.app;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.value.Blob;
import org.apache.isis.applib.value.Clob;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.incode.module.base.dom.MimeTypeData;
import org.incode.module.document.DocumentModule;
import org.incode.module.document.dom.impl.docs.DocumentAbstract;
import org.incode.module.document.dom.impl.docs.DocumentSort;
import org.incode.module.document.dom.impl.docs.DocumentTemplate;
import org.incode.module.document.dom.impl.docs.DocumentTemplateRepository;
import org.incode.module.document.dom.impl.rendering.RenderingStrategy;
import org.incode.module.document.dom.impl.types.DocumentType;
import org.incode.module.document.dom.types.NameType;

import org.estatio.module.base.dom.UdoDomainService;
import org.estatio.module.base.dom.apptenancy.EstatioApplicationTenancyRepository;
import org.estatio.module.invoice.dom.DocumentTypeData;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "org.estatio.app.menus.documents.DocumentTemplateMenu"
)
@DomainServiceLayout(
        named = "Other",
        menuBar = DomainServiceLayout.MenuBar.PRIMARY,
        menuOrder = "900.13")
public class DocumentTemplateMenu extends UdoDomainService<DocumentTemplateMenu> {

    public DocumentTemplateMenu() {
        super(DocumentTemplateMenu.class);
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    @MemberOrder(sequence = "1")
    public DocumentTemplate newTextTemplate(
            final DocumentType type,
            final DocumentTypeData typeData,
            @ParameterLayout(named = "Date", describedAs = "Date that this template comes into effect") final LocalDate date,
            @Parameter(optionality = Optionality.OPTIONAL, maxLength = NameType.Meta.MAX_LEN)
            @ParameterLayout(named = "Name", describedAs = "Optional, will defaults to the name of selected document type") final String name,
            @Parameter(maxLength = DocumentAbstract.MimeTypeType.Meta.MAX_LEN, mustSatisfy = DocumentAbstract.MimeTypeType.Meta.Specification.class)
            @ParameterLayout(named = "MIME type") final String mimeType,
            @Parameter(maxLength = DocumentTemplate.FileSuffixType.Meta.MAX_LEN)
            @ParameterLayout(named = "File suffix", describedAs = "The file suffix for any documents created from this template") final String fileSuffix,
            final ApplicationTenancy applicationTenancy,
            @ParameterLayout(named = "Text", multiLine = DocumentModule.Constants.TEXT_MULTILINE) final String templateText,
            @ParameterLayout(named = "Content Rendering Strategy") final RenderingStrategy contentRenderingStrategy,
            @Parameter(maxLength = DocumentTemplate.NameTextType.Meta.MAX_LEN)
            @ParameterLayout(named = "Subject text") final String subjectText,
            @ParameterLayout(named = "Subject rendering strategy") final RenderingStrategy subjectRenderingStrategy,
            @ParameterLayout(named = "Preview only?") final boolean previewOnly) {

        final String documentName = name != null? name : type.getName();
        return documentTemplateRepository.createText(
                type, typeData, date, applicationTenancy.getPath(), fileSuffix, previewOnly, documentName, mimeType, templateText,
                subjectText, subjectRenderingStrategy);
    }

    public String default4NewTextTemplate() {
        return MimeTypeData.TEXT_HTML.asStr();
    }
    public String default5NewTextTemplate() {
        return "html";
    }
    public List<ApplicationTenancy> choices6NewTextTemplate() {
        return estatioApplicationTenancyRepository.allTenancies();
    }


    public TranslatableString validateNewTextTemplate(
            final DocumentType proposedType,
            final DocumentTypeData typeData,
            final LocalDate proposedDate,
            final String name,
            final String mimeType,
            final String fileSuffix,
            final ApplicationTenancy proposedApplicationTenancy,
            final String templateText,
            final RenderingStrategy contentRenderingStrategy,
            final String subjectText,
            final RenderingStrategy subjectRenderingStrategy,
            final boolean previewOnly) {

        final DocumentSort documentSort = DocumentSort.TEXT;

        return validateNewTemplate(proposedType, typeData, proposedDate, proposedApplicationTenancy, contentRenderingStrategy,
                documentSort);
    }


    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    @MemberOrder(sequence = "2")
    public DocumentTemplate newClobTemplate(
            final DocumentType type,
            final DocumentTypeData typeData,
            @ParameterLayout(named = "Date", describedAs = "Date that this template comes into effect") final LocalDate date,
            @Parameter(optionality = Optionality.OPTIONAL, maxLength = NameType.Meta.MAX_LEN)
            @ParameterLayout(named = "Name", describedAs = "Optional, will default to the file name of the uploaded Clob") final String name,
            @Parameter(maxLength = DocumentTemplate.FileSuffixType.Meta.MAX_LEN)
            @ParameterLayout(named = "File suffix", describedAs = "The file suffix for any documents created from this template") final String fileSuffix,
            final ApplicationTenancy applicationTenancy,
            @Parameter(optionality = Optionality.OPTIONAL) final Clob clob,
            final RenderingStrategy contentRenderingStrategy,
            @Parameter(maxLength = DocumentTemplate.NameTextType.Meta.MAX_LEN)
            @ParameterLayout(named = "Subject text") final String subjectText,
            @ParameterLayout(named = "Subject rendering strategy") final RenderingStrategy subjectRenderingStrategy,
            @ParameterLayout(named = "Preview only?") final boolean previewOnly) {

        final DocumentTemplate template = documentTemplateRepository.createClob(
                type, typeData, date, applicationTenancy.getPath(), fileSuffix, previewOnly, clob, subjectText,
                subjectRenderingStrategy);
        if(name != null) {
            template.setName(name);
        }
        return template;
    }

    public List<ApplicationTenancy> choices5NewClobTemplate() {
        return estatioApplicationTenancyRepository.allTenancies();
    }


    public TranslatableString validateNewClobTemplate(
            final DocumentType proposedType,
            final DocumentTypeData typeData,
            final LocalDate proposedDate,
            final String name,
            final String fileSuffix,
            final ApplicationTenancy proposedApplicationTenancy,
            final Clob clob,
            final RenderingStrategy contentRenderingStrategy,
            final String subjectText,
            final RenderingStrategy subjectRenderingStrategy,
            final boolean previewOnly) {

        final DocumentSort documentSort = DocumentSort.CLOB;

        return validateNewTemplate(
                proposedType, typeData, proposedDate, proposedApplicationTenancy, contentRenderingStrategy,
                documentSort);

    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    @MemberOrder(sequence = "3")
    public DocumentTemplate newBlobTemplate(
            final DocumentType type,
            final DocumentTypeData typeData,
            @ParameterLayout(named = "Date", describedAs = "Date that this template comes into effect") final LocalDate date,
            @Parameter(optionality = Optionality.OPTIONAL, maxLength = NameType.Meta.MAX_LEN)
            @ParameterLayout(named = "Name", describedAs = "Optional, will default to the file name of the uploaded Blob") final String name,
            @Parameter(maxLength = DocumentTemplate.FileSuffixType.Meta.MAX_LEN)
            @ParameterLayout(named = "File suffix", describedAs = "The file suffix for any documents created from this template") final String fileSuffix,
            @Parameter(maxLength = DocumentAbstract.MimeTypeType.Meta.MAX_LEN, mustSatisfy = DocumentAbstract.MimeTypeType.Meta.Specification.class) final ApplicationTenancy applicationTenancy,
            final Blob blob,
            final RenderingStrategy contentRenderingStrategy,
            @Parameter(maxLength = DocumentTemplate.NameTextType.Meta.MAX_LEN)
            @ParameterLayout(named = "Subject text") final String subjectText,
            @ParameterLayout(named = "Subject rendering strategy") final RenderingStrategy subjectRenderingStrategy,
            @ParameterLayout(named = "Preview only?") final boolean previewOnly) {

        final DocumentTemplate template = documentTemplateRepository.createBlob(
                type, typeData, date, applicationTenancy.getPath(), fileSuffix, previewOnly, blob,
                subjectText, subjectRenderingStrategy);
        if(name != null) {
            template.setName(name);
        }
        return template;
    }

    public List<ApplicationTenancy> choices5NewBlobTemplate() {
        return estatioApplicationTenancyRepository.allTenancies();
    }


    public TranslatableString validateNewBlobTemplate(
            final DocumentType proposedType,
            final DocumentTypeData typeData,
            final LocalDate proposedDate,
            final String name,
            final String fileSuffix,
            final ApplicationTenancy proposedApplicationTenancy,
            final Blob blob,
            final RenderingStrategy contentRenderingStrategy,
            final String subjectText,
            final RenderingStrategy subjectRenderingStrategy,
            final boolean previewOnly) {

        final DocumentSort documentSort = DocumentSort.BLOB;

        return validateNewTemplate(
                proposedType, typeData, proposedDate, proposedApplicationTenancy, contentRenderingStrategy, documentSort);
    }

    private TranslatableString validateNewTemplate(
            final DocumentType proposedType,
            final DocumentTypeData typeData,
            final LocalDate proposedDate,
            final ApplicationTenancy proposedApplicationTenancy,
            final RenderingStrategy proposedRenderingStrategy,
            final DocumentSort documentSort) {
        TranslatableString translatableString = documentTemplateRepository.validateApplicationTenancyAndDate(
                proposedType, typeData, proposedApplicationTenancy.getPath(), proposedDate, null);
        if(translatableString != null) {
            return translatableString;
        }

        translatableString = documentTemplateRepository.validateSortAndRenderingStrategyInputNature(documentSort,
                proposedRenderingStrategy);
        if(translatableString != null) {
            return translatableString;
        }

        return null;
    }

    // //////////////////////////////////////

    @Action(semantics = SemanticsOf.SAFE)
    @MemberOrder(sequence = "4")
    public List<DocumentTemplate> allDocumentTemplates() {
        return documentTemplateRepository.allTemplates();
    }


    // //////////////////////////////////////

    @Inject
    private EstatioApplicationTenancyRepository estatioApplicationTenancyRepository;

    @Inject
    private DocumentTemplateRepository documentTemplateRepository;


}
