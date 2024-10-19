/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.maven;

import com.jcabi.log.Logger;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.cactoos.experimental.Threads;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.cactoos.number.SumOf;
import org.eolang.maven.fp.Cache;
import org.eolang.maven.fp.CacheVersion;
import org.eolang.maven.fp.Footprint;
import org.eolang.maven.fp.TojoHash;
import org.eolang.maven.optimization.OptSpy;
import org.eolang.maven.optimization.OptTrain;
import org.eolang.maven.optimization.Optimization;
import org.eolang.maven.tojos.ForeignTojo;
import org.eolang.parser.ParsingTrain;

/**
 * Optimize XML files.
 *
 * @since 0.1
 */
@Mojo(
    name = "optimize",
    defaultPhase = LifecyclePhase.PROCESS_SOURCES,
    threadSafe = true
)
public final class OptimizeMojo extends SafeMojo {

    /**
     * The directory where to transpile to.
     */
    public static final String DIR = "2-optimize";

    /**
     * Subdirectory for optimized cache.
     */
    static final String OPTIMIZED = "optimized";

    /**
     * The directory where to place intermediary files.
     */
    static final String STEPS = "2-optimization-steps";

    /**
     * The map with directories of OptimizeMojo.
     * @checkstyle DiamondOperatorCheck (10 lines)
     */
    @SuppressWarnings("PMD.UseDiamondOperator")
    private static final Map<String, String> DIRECTORIES = new MapOf<String, String>(
        new MapEntry<>(OptimizationFolder.TARGET.getKey(), OptimizeMojo.DIR),
        new MapEntry<>(OptimizationFolder.CACHE.getKey(), OptimizeMojo.OPTIMIZED)
    );

    /**
     * Track optimization steps into intermediate XML files?
     *
     * @since 0.24.0
     * @checkstyle MemberNameCheck (7 lines)
     */
    @SuppressWarnings("PMD.LongVariable")
    @Parameter(property = "eo.trackOptimizationSteps", required = true, defaultValue = "false")
    private boolean trackOptimizationSteps;

    @Override
    public void exec() {
        final Collection<ForeignTojo> tojos = this.scopedTojos().withXmir();
        final int total = new SumOf(
            new Threads<>(
                Runtime.getRuntime().availableProcessors(),
                new Mapped<>(
                    tojo -> () -> this.optimized(tojo, this.optimization()),
                    new Filtered<>(
                        ForeignTojo::notOptimized,
                        this.scopedTojos().withXmir()
                    )
                )
            )
        ).intValue();
        if (total > 0) {
            Logger.info(
                this,
                "Optimized %d out of %d XMIR program(s)", total,
                tojos.size()
            );
        } else {
            Logger.debug(this, "No XMIR programs out of %d optimized", tojos.size());
        }
    }

    /**
     * XMIR optimized to another XMIR.
     * @param tojo Foreign tojo
     * @param optimization Optimization to apply to XMIR
     * @return Amount of optimized XMIR files
     * @throws Exception If fails
     */
    private int optimized(final ForeignTojo tojo, final Optimization optimization)
        throws Exception {
        final Path source = tojo.xmir();
        final XML xmir = new XMLDocument(source);
        final String name = xmir.xpath("/program/@name").get(0);
        final Path base = this.targetDir.toPath().resolve(OptimizeMojo.DIR);
        final Path target = new Place(name).make(base, TranspileMojo.EXT);
        tojo.withOptimized(
            new Footprint(
                source,
                target,
                new Cache(
                    this.cache.resolve(OptimizeMojo.OPTIMIZED),
                    new CacheVersion(
                        this.plugin.getVersion(),
                        new TojoHash(tojo)
                    ),
                    base.relativize(target)
                )
            ).apply(() -> optimization.apply(xmir).toString())
        );
        return 1;
    }

    /**
     * Common optimization for all tojos.
     * @return Optimization for all tojos.
     */
    private Optimization optimization() {
        final Optimization opt;
        if (this.trackOptimizationSteps) {
            opt = new OptSpy(
                new ParsingTrain(),
                this.targetDir.toPath().resolve(OptimizeMojo.STEPS)
            );
        } else {
            opt = new OptTrain(new ParsingTrain());
        }
        return opt;
    }
}
