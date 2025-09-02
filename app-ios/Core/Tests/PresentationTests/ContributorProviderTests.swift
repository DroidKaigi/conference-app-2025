import Dependencies
import Foundation
import Model
@testable import Presentation
import Testing
import UseCase

struct ContributorProviderTests {
    @MainActor
    @Test
    func loadContributorsSuccess() async throws {
        let expectedContributors = TestContributorData.createMockContributors()
        let provider = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(expectedContributors)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorProvider()
        }

        await provider.loadContributors()
        
        #expect(provider.contributors.count == 5)
        #expect(provider.contributors[0].name == "Alice Developer")
        #expect(provider.contributors[1].name == "Bob Engineer")
        #expect(provider.contributors[2].name == "Charlie Designer")
        #expect(provider.contributors[3].name == "David Manager")
        #expect(provider.contributors[4].name == "Eve Architect")
        #expect(provider.isLoading == false)
    }
    
    @MainActor
    @Test
    func loadContributorsEmpty() async throws {
        let provider = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield([])
                    continuation.finish()
                }
            }
        } operation: {
            ContributorProvider()
        }

        await provider.loadContributors()
        
        #expect(provider.contributors.isEmpty)
        #expect(provider.isLoading == false)
    }
    
    @MainActor
    @Test
    func loadContributorsMultipleUpdates() async throws {
        let firstBatch = [
            TestContributorData.createContributor(id: "1", name: "Initial Contributor")
        ]
        
        let secondBatch = [
            TestContributorData.createContributor(id: "2", name: "Updated Contributor 1"),
            TestContributorData.createContributor(id: "3", name: "Updated Contributor 2")
        ]
        
        let provider = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(firstBatch)
                    continuation.yield(secondBatch)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorProvider()
        }

        await provider.loadContributors()
        
        // Should have the latest update (secondBatch)
        #expect(provider.contributors.count == 2)
        #expect(provider.contributors[0].name == "Updated Contributor 1")
        #expect(provider.contributors[1].name == "Updated Contributor 2")
        #expect(provider.isLoading == false)
    }
    
    @MainActor
    @Test
    func loadContributorsIsLoadingState() async throws {
        // Create a simple test that verifies loading state is properly managed
        let testContributors = TestContributorData.createMockContributors()
        
        let provider = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    // Yield data immediately and finish
                    continuation.yield(testContributors)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorProvider()
        }

        #expect(provider.isLoading == false)
        
        await provider.loadContributors()
        
        // After loading completes, isLoading should be false
        #expect(provider.isLoading == false)
        #expect(provider.contributors.count == 5)
    }
    
    @MainActor
    @Test
    func loadContributorsUrlsAreValid() async throws {
        let contributors = TestContributorData.createMockContributors()
        let provider = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(contributors)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorProvider()
        }

        await provider.loadContributors()
        
        // Verify all contributors have valid URLs
        for contributor in provider.contributors {
            #expect(contributor.url.absoluteString.contains("github.com"))
            #expect(contributor.iconUrl.absoluteString.contains("avatars.githubusercontent.com"))
        }
    }
}

enum TestContributorData {
    static func createMockContributors() -> [Model.Contributor] {
        [
            createContributor(id: "1", name: "Alice Developer"),
            createContributor(id: "2", name: "Bob Engineer"),
            createContributor(id: "3", name: "Charlie Designer"),
            createContributor(id: "4", name: "David Manager"),
            createContributor(id: "5", name: "Eve Architect")
        ]
    }
    
    static func createContributor(
        id: String,
        name: String
    ) -> Model.Contributor {
        Model.Contributor(
            id: id,
            name: name,
            url: URL(string: "https://github.com/\(name.lowercased().replacingOccurrences(of: " ", with: ""))")!,
            iconUrl: URL(string: "https://avatars.githubusercontent.com/u/\(id)?v=4")!
        )
    }
}