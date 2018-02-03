package space.zhupeng.compiler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @author zhupeng
 * @date 2018/2/2
 */

public class DialogServiceProcessor extends AbstractProcessor {

    private String mClsName;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AsDialogService.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            return processImpl(annotations, roundEnv);
        } catch (Exception e) {
            // We don't allow exceptions of any kind to propagate to the compiler
            e.printStackTrace();
            return true;
        }
    }

    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
        } else {
            processAnnotations(annotations, roundEnv);
        }

        return true;
    }

    private void processAnnotations(Set<? extends TypeElement> annotations,
                                    RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AsDialogService.class);
        for (Element e : elements) {
            // TODO(gak): check for error trees?
            TypeElement te = (TypeElement) e;
            mClsName = te.getQualifiedName().toString();
        }
    }

    private void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();

        String resourceFile = "META-INF/services/space.zhupeng.arch.widget.dialog.DialogService";
        try {
            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                    resourceFile);
            OutputStream out = fileObject.openOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
            writer.write(mClsName);
            writer.newLine();
            writer.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
